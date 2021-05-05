import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPresionSanguinea, PresionSanguinea } from '../presion-sanguinea.model';
import { PresionSanguineaService } from '../service/presion-sanguinea.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-presion-sanguinea-update',
  templateUrl: './presion-sanguinea-update.component.html',
})
export class PresionSanguineaUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    presionSanguineaSistolica: [],
    presionSanguineaDiastolica: [],
    fechaRegistro: [],
    user: [],
  });

  constructor(
    protected presionSanguineaService: PresionSanguineaService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ presionSanguinea }) => {
      if (presionSanguinea.id === undefined) {
        const today = dayjs().startOf('day');
        presionSanguinea.fechaRegistro = today;
      }

      this.updateForm(presionSanguinea);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const presionSanguinea = this.createFromForm();
    if (presionSanguinea.id !== undefined) {
      this.subscribeToSaveResponse(this.presionSanguineaService.update(presionSanguinea));
    } else {
      this.subscribeToSaveResponse(this.presionSanguineaService.create(presionSanguinea));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPresionSanguinea>>): void {
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

  protected updateForm(presionSanguinea: IPresionSanguinea): void {
    this.editForm.patchValue({
      id: presionSanguinea.id,
      presionSanguineaSistolica: presionSanguinea.presionSanguineaSistolica,
      presionSanguineaDiastolica: presionSanguinea.presionSanguineaDiastolica,
      fechaRegistro: presionSanguinea.fechaRegistro ? presionSanguinea.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      user: presionSanguinea.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, presionSanguinea.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IPresionSanguinea {
    return {
      ...new PresionSanguinea(),
      id: this.editForm.get(['id'])!.value,
      presionSanguineaSistolica: this.editForm.get(['presionSanguineaSistolica'])!.value,
      presionSanguineaDiastolica: this.editForm.get(['presionSanguineaDiastolica'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
