import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAlarma, Alarma } from '../alarma.model';
import { AlarmaService } from '../service/alarma.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-alarma-update',
  templateUrl: './alarma-update.component.html',
})
export class AlarmaUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    timeInstant: [],
    descripcion: [],
    procedimiento: [],
    titulo: [],
    verificar: [],
    observaciones: [],
    prioridad: [],
    user: [],
  });

  constructor(
    protected alarmaService: AlarmaService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alarma }) => {
      if (alarma.id === undefined) {
        const today = dayjs().startOf('day');
        alarma.timeInstant = today;
      }

      this.updateForm(alarma);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alarma = this.createFromForm();
    if (alarma.id !== undefined) {
      this.subscribeToSaveResponse(this.alarmaService.update(alarma));
    } else {
      this.subscribeToSaveResponse(this.alarmaService.create(alarma));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlarma>>): void {
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

  protected updateForm(alarma: IAlarma): void {
    this.editForm.patchValue({
      id: alarma.id,
      timeInstant: alarma.timeInstant ? alarma.timeInstant.format(DATE_TIME_FORMAT) : null,
      descripcion: alarma.descripcion,
      procedimiento: alarma.procedimiento,
      titulo: alarma.titulo,
      verificar: alarma.verificar,
      observaciones: alarma.observaciones,
      prioridad: alarma.prioridad,
      user: alarma.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, alarma.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IAlarma {
    return {
      ...new Alarma(),
      id: this.editForm.get(['id'])!.value,
      timeInstant: this.editForm.get(['timeInstant'])!.value
        ? dayjs(this.editForm.get(['timeInstant'])!.value, DATE_TIME_FORMAT)
        : undefined,
      descripcion: this.editForm.get(['descripcion'])!.value,
      procedimiento: this.editForm.get(['procedimiento'])!.value,
      titulo: this.editForm.get(['titulo'])!.value,
      verificar: this.editForm.get(['verificar'])!.value,
      observaciones: this.editForm.get(['observaciones'])!.value,
      prioridad: this.editForm.get(['prioridad'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
