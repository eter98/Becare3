import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITemperatura, Temperatura } from '../temperatura.model';
import { TemperaturaService } from '../service/temperatura.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-temperatura-update',
  templateUrl: './temperatura-update.component.html',
})
export class TemperaturaUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    temperatura: [],
    fechaRegistro: [],
    user: [],
  });

  constructor(
    protected temperaturaService: TemperaturaService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ temperatura }) => {
      if (temperatura.id === undefined) {
        const today = dayjs().startOf('day');
        temperatura.fechaRegistro = today;
      }

      this.updateForm(temperatura);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const temperatura = this.createFromForm();
    if (temperatura.id !== undefined) {
      this.subscribeToSaveResponse(this.temperaturaService.update(temperatura));
    } else {
      this.subscribeToSaveResponse(this.temperaturaService.create(temperatura));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITemperatura>>): void {
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

  protected updateForm(temperatura: ITemperatura): void {
    this.editForm.patchValue({
      id: temperatura.id,
      temperatura: temperatura.temperatura,
      fechaRegistro: temperatura.fechaRegistro ? temperatura.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      user: temperatura.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, temperatura.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ITemperatura {
    return {
      ...new Temperatura(),
      id: this.editForm.get(['id'])!.value,
      temperatura: this.editForm.get(['temperatura'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
