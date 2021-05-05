import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOximetria, Oximetria } from '../oximetria.model';
import { OximetriaService } from '../service/oximetria.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-oximetria-update',
  templateUrl: './oximetria-update.component.html',
})
export class OximetriaUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    oximetria: [],
    fechaRegistro: [],
    user: [],
  });

  constructor(
    protected oximetriaService: OximetriaService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ oximetria }) => {
      if (oximetria.id === undefined) {
        const today = dayjs().startOf('day');
        oximetria.fechaRegistro = today;
      }

      this.updateForm(oximetria);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const oximetria = this.createFromForm();
    if (oximetria.id !== undefined) {
      this.subscribeToSaveResponse(this.oximetriaService.update(oximetria));
    } else {
      this.subscribeToSaveResponse(this.oximetriaService.create(oximetria));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOximetria>>): void {
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

  protected updateForm(oximetria: IOximetria): void {
    this.editForm.patchValue({
      id: oximetria.id,
      oximetria: oximetria.oximetria,
      fechaRegistro: oximetria.fechaRegistro ? oximetria.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      user: oximetria.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, oximetria.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IOximetria {
    return {
      ...new Oximetria(),
      id: this.editForm.get(['id'])!.value,
      oximetria: this.editForm.get(['oximetria'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
