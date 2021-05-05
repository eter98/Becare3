import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IIngesta, Ingesta } from '../ingesta.model';
import { IngestaService } from '../service/ingesta.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-ingesta-update',
  templateUrl: './ingesta-update.component.html',
})
export class IngestaUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    tipo: [],
    consumoCalorias: [],
    fechaRegistro: [],
    user: [],
  });

  constructor(
    protected ingestaService: IngestaService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ingesta }) => {
      if (ingesta.id === undefined) {
        const today = dayjs().startOf('day');
        ingesta.fechaRegistro = today;
      }

      this.updateForm(ingesta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ingesta = this.createFromForm();
    if (ingesta.id !== undefined) {
      this.subscribeToSaveResponse(this.ingestaService.update(ingesta));
    } else {
      this.subscribeToSaveResponse(this.ingestaService.create(ingesta));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIngesta>>): void {
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

  protected updateForm(ingesta: IIngesta): void {
    this.editForm.patchValue({
      id: ingesta.id,
      tipo: ingesta.tipo,
      consumoCalorias: ingesta.consumoCalorias,
      fechaRegistro: ingesta.fechaRegistro ? ingesta.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      user: ingesta.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, ingesta.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IIngesta {
    return {
      ...new Ingesta(),
      id: this.editForm.get(['id'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      consumoCalorias: this.editForm.get(['consumoCalorias'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
