import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITratamientoMedicamento, TratamientoMedicamento } from '../tratamiento-medicamento.model';
import { TratamientoMedicamentoService } from '../service/tratamiento-medicamento.service';
import { ITratamieto } from 'app/entities/tratamieto/tratamieto.model';
import { TratamietoService } from 'app/entities/tratamieto/service/tratamieto.service';
import { IMedicamento } from 'app/entities/medicamento/medicamento.model';
import { MedicamentoService } from 'app/entities/medicamento/service/medicamento.service';

@Component({
  selector: 'jhi-tratamiento-medicamento-update',
  templateUrl: './tratamiento-medicamento-update.component.html',
})
export class TratamientoMedicamentoUpdateComponent implements OnInit {
  isSaving = false;

  tratamietosSharedCollection: ITratamieto[] = [];
  medicamentosSharedCollection: IMedicamento[] = [];

  editForm = this.fb.group({
    id: [],
    dosis: [],
    intensidad: [],
    tratamieto: [],
    medicamento: [],
  });

  constructor(
    protected tratamientoMedicamentoService: TratamientoMedicamentoService,
    protected tratamietoService: TratamietoService,
    protected medicamentoService: MedicamentoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tratamientoMedicamento }) => {
      this.updateForm(tratamientoMedicamento);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tratamientoMedicamento = this.createFromForm();
    if (tratamientoMedicamento.id !== undefined) {
      this.subscribeToSaveResponse(this.tratamientoMedicamentoService.update(tratamientoMedicamento));
    } else {
      this.subscribeToSaveResponse(this.tratamientoMedicamentoService.create(tratamientoMedicamento));
    }
  }

  trackTratamietoById(index: number, item: ITratamieto): number {
    return item.id!;
  }

  trackMedicamentoById(index: number, item: IMedicamento): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITratamientoMedicamento>>): void {
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

  protected updateForm(tratamientoMedicamento: ITratamientoMedicamento): void {
    this.editForm.patchValue({
      id: tratamientoMedicamento.id,
      dosis: tratamientoMedicamento.dosis,
      intensidad: tratamientoMedicamento.intensidad,
      tratamieto: tratamientoMedicamento.tratamieto,
      medicamento: tratamientoMedicamento.medicamento,
    });

    this.tratamietosSharedCollection = this.tratamietoService.addTratamietoToCollectionIfMissing(
      this.tratamietosSharedCollection,
      tratamientoMedicamento.tratamieto
    );
    this.medicamentosSharedCollection = this.medicamentoService.addMedicamentoToCollectionIfMissing(
      this.medicamentosSharedCollection,
      tratamientoMedicamento.medicamento
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tratamietoService
      .query()
      .pipe(map((res: HttpResponse<ITratamieto[]>) => res.body ?? []))
      .pipe(
        map((tratamietos: ITratamieto[]) =>
          this.tratamietoService.addTratamietoToCollectionIfMissing(tratamietos, this.editForm.get('tratamieto')!.value)
        )
      )
      .subscribe((tratamietos: ITratamieto[]) => (this.tratamietosSharedCollection = tratamietos));

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

  protected createFromForm(): ITratamientoMedicamento {
    return {
      ...new TratamientoMedicamento(),
      id: this.editForm.get(['id'])!.value,
      dosis: this.editForm.get(['dosis'])!.value,
      intensidad: this.editForm.get(['intensidad'])!.value,
      tratamieto: this.editForm.get(['tratamieto'])!.value,
      medicamento: this.editForm.get(['medicamento'])!.value,
    };
  }
}
