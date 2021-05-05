import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISueno, Sueno } from '../sueno.model';
import { SuenoService } from '../service/sueno.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-sueno-update',
  templateUrl: './sueno-update.component.html',
})
export class SuenoUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    superficial: [],
    profundo: [],
    despierto: [],
    timeInstant: [],
    user: [],
  });

  constructor(
    protected suenoService: SuenoService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sueno }) => {
      if (sueno.id === undefined) {
        const today = dayjs().startOf('day');
        sueno.timeInstant = today;
      }

      this.updateForm(sueno);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sueno = this.createFromForm();
    if (sueno.id !== undefined) {
      this.subscribeToSaveResponse(this.suenoService.update(sueno));
    } else {
      this.subscribeToSaveResponse(this.suenoService.create(sueno));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISueno>>): void {
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

  protected updateForm(sueno: ISueno): void {
    this.editForm.patchValue({
      id: sueno.id,
      superficial: sueno.superficial,
      profundo: sueno.profundo,
      despierto: sueno.despierto,
      timeInstant: sueno.timeInstant ? sueno.timeInstant.format(DATE_TIME_FORMAT) : null,
      user: sueno.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, sueno.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ISueno {
    return {
      ...new Sueno(),
      id: this.editForm.get(['id'])!.value,
      superficial: this.editForm.get(['superficial'])!.value,
      profundo: this.editForm.get(['profundo'])!.value,
      despierto: this.editForm.get(['despierto'])!.value,
      timeInstant: this.editForm.get(['timeInstant'])!.value
        ? dayjs(this.editForm.get(['timeInstant'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
