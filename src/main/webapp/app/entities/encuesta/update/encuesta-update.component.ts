import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEncuesta, Encuesta } from '../encuesta.model';
import { EncuestaService } from '../service/encuesta.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-encuesta-update',
  templateUrl: './encuesta-update.component.html',
})
export class EncuestaUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    fecha: [],
    debilidad: [],
    cefalea: [],
    calambres: [],
    nauseas: [],
    vomito: [],
    mareo: [],
    ninguna: [],
    user: [],
  });

  constructor(
    protected encuestaService: EncuestaService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ encuesta }) => {
      if (encuesta.id === undefined) {
        const today = dayjs().startOf('day');
        encuesta.fecha = today;
      }

      this.updateForm(encuesta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const encuesta = this.createFromForm();
    if (encuesta.id !== undefined) {
      this.subscribeToSaveResponse(this.encuestaService.update(encuesta));
    } else {
      this.subscribeToSaveResponse(this.encuestaService.create(encuesta));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEncuesta>>): void {
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

  protected updateForm(encuesta: IEncuesta): void {
    this.editForm.patchValue({
      id: encuesta.id,
      fecha: encuesta.fecha ? encuesta.fecha.format(DATE_TIME_FORMAT) : null,
      debilidad: encuesta.debilidad,
      cefalea: encuesta.cefalea,
      calambres: encuesta.calambres,
      nauseas: encuesta.nauseas,
      vomito: encuesta.vomito,
      mareo: encuesta.mareo,
      ninguna: encuesta.ninguna,
      user: encuesta.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, encuesta.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IEncuesta {
    return {
      ...new Encuesta(),
      id: this.editForm.get(['id'])!.value,
      fecha: this.editForm.get(['fecha'])!.value ? dayjs(this.editForm.get(['fecha'])!.value, DATE_TIME_FORMAT) : undefined,
      debilidad: this.editForm.get(['debilidad'])!.value,
      cefalea: this.editForm.get(['cefalea'])!.value,
      calambres: this.editForm.get(['calambres'])!.value,
      nauseas: this.editForm.get(['nauseas'])!.value,
      vomito: this.editForm.get(['vomito'])!.value,
      mareo: this.editForm.get(['mareo'])!.value,
      ninguna: this.editForm.get(['ninguna'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
