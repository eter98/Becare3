import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPeso, Peso } from '../peso.model';
import { PesoService } from '../service/peso.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-peso-update',
  templateUrl: './peso-update.component.html',
})
export class PesoUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    pesoKG: [],
    descripcion: [],
    fechaRegistro: [],
    user: [],
  });

  constructor(
    protected pesoService: PesoService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ peso }) => {
      if (peso.id === undefined) {
        const today = dayjs().startOf('day');
        peso.fechaRegistro = today;
      }

      this.updateForm(peso);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const peso = this.createFromForm();
    if (peso.id !== undefined) {
      this.subscribeToSaveResponse(this.pesoService.update(peso));
    } else {
      this.subscribeToSaveResponse(this.pesoService.create(peso));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeso>>): void {
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

  protected updateForm(peso: IPeso): void {
    this.editForm.patchValue({
      id: peso.id,
      pesoKG: peso.pesoKG,
      descripcion: peso.descripcion,
      fechaRegistro: peso.fechaRegistro ? peso.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      user: peso.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, peso.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IPeso {
    return {
      ...new Peso(),
      id: this.editForm.get(['id'])!.value,
      pesoKG: this.editForm.get(['pesoKG'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
