import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICondicion, Condicion } from '../condicion.model';
import { CondicionService } from '../service/condicion.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-condicion-update',
  templateUrl: './condicion-update.component.html',
})
export class CondicionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    condicion: [],
    descripcion: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected condicionService: CondicionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ condicion }) => {
      this.updateForm(condicion);
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
    const condicion = this.createFromForm();
    if (condicion.id !== undefined) {
      this.subscribeToSaveResponse(this.condicionService.update(condicion));
    } else {
      this.subscribeToSaveResponse(this.condicionService.create(condicion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICondicion>>): void {
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

  protected updateForm(condicion: ICondicion): void {
    this.editForm.patchValue({
      id: condicion.id,
      condicion: condicion.condicion,
      descripcion: condicion.descripcion,
    });
  }

  protected createFromForm(): ICondicion {
    return {
      ...new Condicion(),
      id: this.editForm.get(['id'])!.value,
      condicion: this.editForm.get(['condicion'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
    };
  }
}
