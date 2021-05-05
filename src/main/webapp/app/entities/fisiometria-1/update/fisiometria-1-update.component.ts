import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFisiometria1, Fisiometria1 } from '../fisiometria-1.model';
import { Fisiometria1Service } from '../service/fisiometria-1.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-fisiometria-1-update',
  templateUrl: './fisiometria-1-update.component.html',
})
export class Fisiometria1UpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    ritmoCardiaco: [],
    ritmoRespiratorio: [],
    oximetria: [],
    presionArterialSistolica: [],
    presionArterialDiastolica: [],
    temperatura: [],
    fechaRegistro: [],
    fechaToma: [],
    user: [],
  });

  constructor(
    protected fisiometria1Service: Fisiometria1Service,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fisiometria1 }) => {
      if (fisiometria1.id === undefined) {
        const today = dayjs().startOf('day');
        fisiometria1.fechaRegistro = today;
        fisiometria1.fechaToma = today;
      }

      this.updateForm(fisiometria1);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fisiometria1 = this.createFromForm();
    if (fisiometria1.id !== undefined) {
      this.subscribeToSaveResponse(this.fisiometria1Service.update(fisiometria1));
    } else {
      this.subscribeToSaveResponse(this.fisiometria1Service.create(fisiometria1));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFisiometria1>>): void {
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

  protected updateForm(fisiometria1: IFisiometria1): void {
    this.editForm.patchValue({
      id: fisiometria1.id,
      ritmoCardiaco: fisiometria1.ritmoCardiaco,
      ritmoRespiratorio: fisiometria1.ritmoRespiratorio,
      oximetria: fisiometria1.oximetria,
      presionArterialSistolica: fisiometria1.presionArterialSistolica,
      presionArterialDiastolica: fisiometria1.presionArterialDiastolica,
      temperatura: fisiometria1.temperatura,
      fechaRegistro: fisiometria1.fechaRegistro ? fisiometria1.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      fechaToma: fisiometria1.fechaToma ? fisiometria1.fechaToma.format(DATE_TIME_FORMAT) : null,
      user: fisiometria1.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, fisiometria1.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IFisiometria1 {
    return {
      ...new Fisiometria1(),
      id: this.editForm.get(['id'])!.value,
      ritmoCardiaco: this.editForm.get(['ritmoCardiaco'])!.value,
      ritmoRespiratorio: this.editForm.get(['ritmoRespiratorio'])!.value,
      oximetria: this.editForm.get(['oximetria'])!.value,
      presionArterialSistolica: this.editForm.get(['presionArterialSistolica'])!.value,
      presionArterialDiastolica: this.editForm.get(['presionArterialDiastolica'])!.value,
      temperatura: this.editForm.get(['temperatura'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaToma: this.editForm.get(['fechaToma'])!.value ? dayjs(this.editForm.get(['fechaToma'])!.value, DATE_TIME_FORMAT) : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
