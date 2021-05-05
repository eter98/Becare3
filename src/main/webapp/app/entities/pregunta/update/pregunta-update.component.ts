import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPregunta, Pregunta } from '../pregunta.model';
import { PreguntaService } from '../service/pregunta.service';
import { ICondicion } from 'app/entities/condicion/condicion.model';
import { CondicionService } from 'app/entities/condicion/service/condicion.service';

@Component({
  selector: 'jhi-pregunta-update',
  templateUrl: './pregunta-update.component.html',
})
export class PreguntaUpdateComponent implements OnInit {
  isSaving = false;

  condicionsSharedCollection: ICondicion[] = [];

  editForm = this.fb.group({
    id: [],
    pregunta: [],
    condicion: [],
  });

  constructor(
    protected preguntaService: PreguntaService,
    protected condicionService: CondicionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pregunta }) => {
      this.updateForm(pregunta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pregunta = this.createFromForm();
    if (pregunta.id !== undefined) {
      this.subscribeToSaveResponse(this.preguntaService.update(pregunta));
    } else {
      this.subscribeToSaveResponse(this.preguntaService.create(pregunta));
    }
  }

  trackCondicionById(index: number, item: ICondicion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPregunta>>): void {
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

  protected updateForm(pregunta: IPregunta): void {
    this.editForm.patchValue({
      id: pregunta.id,
      pregunta: pregunta.pregunta,
      condicion: pregunta.condicion,
    });

    this.condicionsSharedCollection = this.condicionService.addCondicionToCollectionIfMissing(
      this.condicionsSharedCollection,
      pregunta.condicion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.condicionService
      .query()
      .pipe(map((res: HttpResponse<ICondicion[]>) => res.body ?? []))
      .pipe(
        map((condicions: ICondicion[]) =>
          this.condicionService.addCondicionToCollectionIfMissing(condicions, this.editForm.get('condicion')!.value)
        )
      )
      .subscribe((condicions: ICondicion[]) => (this.condicionsSharedCollection = condicions));
  }

  protected createFromForm(): IPregunta {
    return {
      ...new Pregunta(),
      id: this.editForm.get(['id'])!.value,
      pregunta: this.editForm.get(['pregunta'])!.value,
      condicion: this.editForm.get(['condicion'])!.value,
    };
  }
}
