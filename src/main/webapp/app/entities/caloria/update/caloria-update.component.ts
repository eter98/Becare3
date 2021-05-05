import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICaloria, Caloria } from '../caloria.model';
import { CaloriaService } from '../service/caloria.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-caloria-update',
  templateUrl: './caloria-update.component.html',
})
export class CaloriaUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    caloriasActivas: [],
    descripcion: [],
    fechaRegistro: [],
    user: [],
  });

  constructor(
    protected caloriaService: CaloriaService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ caloria }) => {
      if (caloria.id === undefined) {
        const today = dayjs().startOf('day');
        caloria.fechaRegistro = today;
      }

      this.updateForm(caloria);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const caloria = this.createFromForm();
    if (caloria.id !== undefined) {
      this.subscribeToSaveResponse(this.caloriaService.update(caloria));
    } else {
      this.subscribeToSaveResponse(this.caloriaService.create(caloria));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICaloria>>): void {
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

  protected updateForm(caloria: ICaloria): void {
    this.editForm.patchValue({
      id: caloria.id,
      caloriasActivas: caloria.caloriasActivas,
      descripcion: caloria.descripcion,
      fechaRegistro: caloria.fechaRegistro ? caloria.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      user: caloria.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, caloria.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ICaloria {
    return {
      ...new Caloria(),
      id: this.editForm.get(['id'])!.value,
      caloriasActivas: this.editForm.get(['caloriasActivas'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
