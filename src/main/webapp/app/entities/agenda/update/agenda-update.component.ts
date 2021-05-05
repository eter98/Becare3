import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAgenda, Agenda } from '../agenda.model';
import { AgendaService } from '../service/agenda.service';
import { IMedicamento } from 'app/entities/medicamento/medicamento.model';
import { MedicamentoService } from 'app/entities/medicamento/service/medicamento.service';

@Component({
  selector: 'jhi-agenda-update',
  templateUrl: './agenda-update.component.html',
})
export class AgendaUpdateComponent implements OnInit {
  isSaving = false;

  medicamentosSharedCollection: IMedicamento[] = [];

  editForm = this.fb.group({
    id: [],
    horaMedicamento: [],
    medicamento: [],
  });

  constructor(
    protected agendaService: AgendaService,
    protected medicamentoService: MedicamentoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agenda }) => {
      this.updateForm(agenda);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agenda = this.createFromForm();
    if (agenda.id !== undefined) {
      this.subscribeToSaveResponse(this.agendaService.update(agenda));
    } else {
      this.subscribeToSaveResponse(this.agendaService.create(agenda));
    }
  }

  trackMedicamentoById(index: number, item: IMedicamento): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgenda>>): void {
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

  protected updateForm(agenda: IAgenda): void {
    this.editForm.patchValue({
      id: agenda.id,
      horaMedicamento: agenda.horaMedicamento,
      medicamento: agenda.medicamento,
    });

    this.medicamentosSharedCollection = this.medicamentoService.addMedicamentoToCollectionIfMissing(
      this.medicamentosSharedCollection,
      agenda.medicamento
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
  }

  protected createFromForm(): IAgenda {
    return {
      ...new Agenda(),
      id: this.editForm.get(['id'])!.value,
      horaMedicamento: this.editForm.get(['horaMedicamento'])!.value,
      medicamento: this.editForm.get(['medicamento'])!.value,
    };
  }
}
