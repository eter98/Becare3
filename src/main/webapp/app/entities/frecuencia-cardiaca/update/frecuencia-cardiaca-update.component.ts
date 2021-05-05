import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFrecuenciaCardiaca, FrecuenciaCardiaca } from '../frecuencia-cardiaca.model';
import { FrecuenciaCardiacaService } from '../service/frecuencia-cardiaca.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-frecuencia-cardiaca-update',
  templateUrl: './frecuencia-cardiaca-update.component.html',
})
export class FrecuenciaCardiacaUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    frecuenciaCardiaca: [],
    fechaRegistro: [],
    user: [],
  });

  constructor(
    protected frecuenciaCardiacaService: FrecuenciaCardiacaService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ frecuenciaCardiaca }) => {
      if (frecuenciaCardiaca.id === undefined) {
        const today = dayjs().startOf('day');
        frecuenciaCardiaca.fechaRegistro = today;
      }

      this.updateForm(frecuenciaCardiaca);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const frecuenciaCardiaca = this.createFromForm();
    if (frecuenciaCardiaca.id !== undefined) {
      this.subscribeToSaveResponse(this.frecuenciaCardiacaService.update(frecuenciaCardiaca));
    } else {
      this.subscribeToSaveResponse(this.frecuenciaCardiacaService.create(frecuenciaCardiaca));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFrecuenciaCardiaca>>): void {
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

  protected updateForm(frecuenciaCardiaca: IFrecuenciaCardiaca): void {
    this.editForm.patchValue({
      id: frecuenciaCardiaca.id,
      frecuenciaCardiaca: frecuenciaCardiaca.frecuenciaCardiaca,
      fechaRegistro: frecuenciaCardiaca.fechaRegistro ? frecuenciaCardiaca.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      user: frecuenciaCardiaca.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, frecuenciaCardiaca.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IFrecuenciaCardiaca {
    return {
      ...new FrecuenciaCardiaca(),
      id: this.editForm.get(['id'])!.value,
      frecuenciaCardiaca: this.editForm.get(['frecuenciaCardiaca'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
