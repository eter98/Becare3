import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMedicamento, Medicamento } from '../medicamento.model';
import { MedicamentoService } from '../service/medicamento.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-medicamento-update',
  templateUrl: './medicamento-update.component.html',
})
export class MedicamentoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    descripcion: [],
    fechaIngreso: [],
    presentacion: [],
    generico: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected medicamentoService: MedicamentoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicamento }) => {
      if (medicamento.id === undefined) {
        const today = dayjs().startOf('day');
        medicamento.fechaIngreso = today;
      }

      this.updateForm(medicamento);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('becare3App.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicamento = this.createFromForm();
    if (medicamento.id !== undefined) {
      this.subscribeToSaveResponse(this.medicamentoService.update(medicamento));
    } else {
      this.subscribeToSaveResponse(this.medicamentoService.create(medicamento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicamento>>): void {
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

  protected updateForm(medicamento: IMedicamento): void {
    this.editForm.patchValue({
      id: medicamento.id,
      nombre: medicamento.nombre,
      descripcion: medicamento.descripcion,
      fechaIngreso: medicamento.fechaIngreso ? medicamento.fechaIngreso.format(DATE_TIME_FORMAT) : null,
      presentacion: medicamento.presentacion,
      generico: medicamento.generico,
    });
  }

  protected createFromForm(): IMedicamento {
    return {
      ...new Medicamento(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      fechaIngreso: this.editForm.get(['fechaIngreso'])!.value
        ? dayjs(this.editForm.get(['fechaIngreso'])!.value, DATE_TIME_FORMAT)
        : undefined,
      presentacion: this.editForm.get(['presentacion'])!.value,
      generico: this.editForm.get(['generico'])!.value,
    };
  }
}
