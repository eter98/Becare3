import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAdherencia, Adherencia } from '../adherencia.model';
import { AdherenciaService } from '../service/adherencia.service';
import { IMedicamento } from 'app/entities/medicamento/medicamento.model';
import { MedicamentoService } from 'app/entities/medicamento/service/medicamento.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';

@Component({
  selector: 'jhi-adherencia-update',
  templateUrl: './adherencia-update.component.html',
})
export class AdherenciaUpdateComponent implements OnInit {
  isSaving = false;

  medicamentosSharedCollection: IMedicamento[] = [];
  pacientesSharedCollection: IPaciente[] = [];

  editForm = this.fb.group({
    id: [],
    horaToma: [],
    respuesta: [],
    valor: [],
    comentario: [],
    medicamento: [],
    paciente: [],
  });

  constructor(
    protected adherenciaService: AdherenciaService,
    protected medicamentoService: MedicamentoService,
    protected pacienteService: PacienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adherencia }) => {
      if (adherencia.id === undefined) {
        const today = dayjs().startOf('day');
        adherencia.horaToma = today;
      }

      this.updateForm(adherencia);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const adherencia = this.createFromForm();
    if (adherencia.id !== undefined) {
      this.subscribeToSaveResponse(this.adherenciaService.update(adherencia));
    } else {
      this.subscribeToSaveResponse(this.adherenciaService.create(adherencia));
    }
  }

  trackMedicamentoById(index: number, item: IMedicamento): number {
    return item.id!;
  }

  trackPacienteById(index: number, item: IPaciente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdherencia>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(adherencia: IAdherencia): void {
    this.editForm.patchValue({
      id: adherencia.id,
      horaToma: adherencia.horaToma ? adherencia.horaToma.format(DATE_TIME_FORMAT) : null,
      respuesta: adherencia.respuesta,
      valor: adherencia.valor,
      comentario: adherencia.comentario,
      medicamento: adherencia.medicamento,
      paciente: adherencia.paciente,
    });

    this.medicamentosSharedCollection = this.medicamentoService.addMedicamentoToCollectionIfMissing(
      this.medicamentosSharedCollection,
      adherencia.medicamento
    );
    this.pacientesSharedCollection = this.pacienteService.addPacienteToCollectionIfMissing(
      this.pacientesSharedCollection,
      adherencia.paciente
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicamentoService
      .query()
      .pipe(map((res: HttpResponse<IMedicamento[]>) => res.body ?? []))
      .pipe(
        map((medicamentos: IMedicamento[]) =>
          this.medicamentoService.addMedicamentoToCollectionIfMissing(medicamentos, this.editForm.get('medicamento')!.value)
        )
      )
      .subscribe((medicamentos: IMedicamento[]) => (this.medicamentosSharedCollection = medicamentos));

    this.pacienteService
      .query()
      .pipe(map((res: HttpResponse<IPaciente[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPaciente[]) =>
          this.pacienteService.addPacienteToCollectionIfMissing(pacientes, this.editForm.get('paciente')!.value)
        )
      )
      .subscribe((pacientes: IPaciente[]) => (this.pacientesSharedCollection = pacientes));
  }

  protected createFromForm(): IAdherencia {
    return {
      ...new Adherencia(),
      id: this.editForm.get(['id'])!.value,
      horaToma: this.editForm.get(['horaToma'])!.value ? dayjs(this.editForm.get(['horaToma'])!.value, DATE_TIME_FORMAT) : undefined,
      respuesta: this.editForm.get(['respuesta'])!.value,
      valor: this.editForm.get(['valor'])!.value,
      comentario: this.editForm.get(['comentario'])!.value,
      medicamento: this.editForm.get(['medicamento'])!.value,
      paciente: this.editForm.get(['paciente'])!.value,
    };
  }
}
