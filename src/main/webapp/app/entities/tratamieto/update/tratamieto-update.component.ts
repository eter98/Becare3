import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITratamieto, Tratamieto } from '../tratamieto.model';
import { TratamietoService } from '../service/tratamieto.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-tratamieto-update',
  templateUrl: './tratamieto-update.component.html',
})
export class TratamietoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    descripcionTratamiento: [],
    fechaInicio: [],
    fechaFin: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected tratamietoService: TratamietoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tratamieto }) => {
      if (tratamieto.id === undefined) {
        const today = dayjs().startOf('day');
        tratamieto.fechaInicio = today;
        tratamieto.fechaFin = today;
      }

      this.updateForm(tratamieto);
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
    const tratamieto = this.createFromForm();
    if (tratamieto.id !== undefined) {
      this.subscribeToSaveResponse(this.tratamietoService.update(tratamieto));
    } else {
      this.subscribeToSaveResponse(this.tratamietoService.create(tratamieto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITratamieto>>): void {
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

  protected updateForm(tratamieto: ITratamieto): void {
    this.editForm.patchValue({
      id: tratamieto.id,
      descripcionTratamiento: tratamieto.descripcionTratamiento,
      fechaInicio: tratamieto.fechaInicio ? tratamieto.fechaInicio.format(DATE_TIME_FORMAT) : null,
      fechaFin: tratamieto.fechaFin ? tratamieto.fechaFin.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): ITratamieto {
    return {
      ...new Tratamieto(),
      id: this.editForm.get(['id'])!.value,
      descripcionTratamiento: this.editForm.get(['descripcionTratamiento'])!.value,
      fechaInicio: this.editForm.get(['fechaInicio'])!.value
        ? dayjs(this.editForm.get(['fechaInicio'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaFin: this.editForm.get(['fechaFin'])!.value ? dayjs(this.editForm.get(['fechaFin'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
