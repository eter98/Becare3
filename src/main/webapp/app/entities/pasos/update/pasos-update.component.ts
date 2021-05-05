import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPasos, Pasos } from '../pasos.model';
import { PasosService } from '../service/pasos.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-pasos-update',
  templateUrl: './pasos-update.component.html',
})
export class PasosUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nroPasos: [],
    timeInstant: [],
    user: [],
  });

  constructor(
    protected pasosService: PasosService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pasos }) => {
      if (pasos.id === undefined) {
        const today = dayjs().startOf('day');
        pasos.timeInstant = today;
      }

      this.updateForm(pasos);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pasos = this.createFromForm();
    if (pasos.id !== undefined) {
      this.subscribeToSaveResponse(this.pasosService.update(pasos));
    } else {
      this.subscribeToSaveResponse(this.pasosService.create(pasos));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPasos>>): void {
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

  protected updateForm(pasos: IPasos): void {
    this.editForm.patchValue({
      id: pasos.id,
      nroPasos: pasos.nroPasos,
      timeInstant: pasos.timeInstant ? pasos.timeInstant.format(DATE_TIME_FORMAT) : null,
      user: pasos.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, pasos.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IPasos {
    return {
      ...new Pasos(),
      id: this.editForm.get(['id'])!.value,
      nroPasos: this.editForm.get(['nroPasos'])!.value,
      timeInstant: this.editForm.get(['timeInstant'])!.value
        ? dayjs(this.editForm.get(['timeInstant'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
