import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { INotificacion, Notificacion } from '../notificacion.model';
import { NotificacionService } from '../service/notificacion.service';
import { ITokenDisp } from 'app/entities/token-disp/token-disp.model';
import { TokenDispService } from 'app/entities/token-disp/service/token-disp.service';

@Component({
  selector: 'jhi-notificacion-update',
  templateUrl: './notificacion-update.component.html',
})
export class NotificacionUpdateComponent implements OnInit {
  isSaving = false;

  tokenDispsSharedCollection: ITokenDisp[] = [];

  editForm = this.fb.group({
    id: [],
    fechaInicio: [],
    fechaActualizacion: [],
    estado: [],
    tipoNotificacion: [],
    token: [],
  });

  constructor(
    protected notificacionService: NotificacionService,
    protected tokenDispService: TokenDispService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificacion }) => {
      if (notificacion.id === undefined) {
        const today = dayjs().startOf('day');
        notificacion.fechaInicio = today;
        notificacion.fechaActualizacion = today;
      }

      this.updateForm(notificacion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notificacion = this.createFromForm();
    if (notificacion.id !== undefined) {
      this.subscribeToSaveResponse(this.notificacionService.update(notificacion));
    } else {
      this.subscribeToSaveResponse(this.notificacionService.create(notificacion));
    }
  }

  trackTokenDispById(index: number, item: ITokenDisp): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotificacion>>): void {
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

  protected updateForm(notificacion: INotificacion): void {
    this.editForm.patchValue({
      id: notificacion.id,
      fechaInicio: notificacion.fechaInicio ? notificacion.fechaInicio.format(DATE_TIME_FORMAT) : null,
      fechaActualizacion: notificacion.fechaActualizacion ? notificacion.fechaActualizacion.format(DATE_TIME_FORMAT) : null,
      estado: notificacion.estado,
      tipoNotificacion: notificacion.tipoNotificacion,
      token: notificacion.token,
    });

    this.tokenDispsSharedCollection = this.tokenDispService.addTokenDispToCollectionIfMissing(
      this.tokenDispsSharedCollection,
      notificacion.token
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tokenDispService
      .query()
      .pipe(map((res: HttpResponse<ITokenDisp[]>) => res.body ?? []))
      .pipe(
        map((tokenDisps: ITokenDisp[]) =>
          this.tokenDispService.addTokenDispToCollectionIfMissing(tokenDisps, this.editForm.get('token')!.value)
        )
      )
      .subscribe((tokenDisps: ITokenDisp[]) => (this.tokenDispsSharedCollection = tokenDisps));
  }

  protected createFromForm(): INotificacion {
    return {
      ...new Notificacion(),
      id: this.editForm.get(['id'])!.value,
      fechaInicio: this.editForm.get(['fechaInicio'])!.value
        ? dayjs(this.editForm.get(['fechaInicio'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaActualizacion: this.editForm.get(['fechaActualizacion'])!.value
        ? dayjs(this.editForm.get(['fechaActualizacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      estado: this.editForm.get(['estado'])!.value,
      tipoNotificacion: this.editForm.get(['tipoNotificacion'])!.value,
      token: this.editForm.get(['token'])!.value,
    };
  }
}
