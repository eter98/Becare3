import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPrograma, Programa } from '../programa.model';
import { ProgramaService } from '../service/programa.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-programa-update',
  templateUrl: './programa-update.component.html',
})
export class ProgramaUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    caloriasActividad: [],
    pasosActividad: [],
    fechaRegistro: [],
    user: [],
  });

  constructor(
    protected programaService: ProgramaService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ programa }) => {
      if (programa.id === undefined) {
        const today = dayjs().startOf('day');
        programa.fechaRegistro = today;
      }

      this.updateForm(programa);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const programa = this.createFromForm();
    if (programa.id !== undefined) {
      this.subscribeToSaveResponse(this.programaService.update(programa));
    } else {
      this.subscribeToSaveResponse(this.programaService.create(programa));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrograma>>): void {
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

  protected updateForm(programa: IPrograma): void {
    this.editForm.patchValue({
      id: programa.id,
      caloriasActividad: programa.caloriasActividad,
      pasosActividad: programa.pasosActividad,
      fechaRegistro: programa.fechaRegistro ? programa.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      user: programa.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, programa.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IPrograma {
    return {
      ...new Programa(),
      id: this.editForm.get(['id'])!.value,
      caloriasActividad: this.editForm.get(['caloriasActividad'])!.value,
      pasosActividad: this.editForm.get(['pasosActividad'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
