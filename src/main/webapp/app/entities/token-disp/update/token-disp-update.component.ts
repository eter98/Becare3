import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITokenDisp, TokenDisp } from '../token-disp.model';
import { TokenDispService } from '../service/token-disp.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-token-disp-update',
  templateUrl: './token-disp-update.component.html',
})
export class TokenDispUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    tokenConexion: [],
    activo: [],
    fechaInicio: [],
    fechaFin: [],
    user: [],
  });

  constructor(
    protected tokenDispService: TokenDispService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tokenDisp }) => {
      if (tokenDisp.id === undefined) {
        const today = dayjs().startOf('day');
        tokenDisp.fechaInicio = today;
        tokenDisp.fechaFin = today;
      }

      this.updateForm(tokenDisp);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tokenDisp = this.createFromForm();
    if (tokenDisp.id !== undefined) {
      this.subscribeToSaveResponse(this.tokenDispService.update(tokenDisp));
    } else {
      this.subscribeToSaveResponse(this.tokenDispService.create(tokenDisp));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITokenDisp>>): void {
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

  protected updateForm(tokenDisp: ITokenDisp): void {
    this.editForm.patchValue({
      id: tokenDisp.id,
      tokenConexion: tokenDisp.tokenConexion,
      activo: tokenDisp.activo,
      fechaInicio: tokenDisp.fechaInicio ? tokenDisp.fechaInicio.format(DATE_TIME_FORMAT) : null,
      fechaFin: tokenDisp.fechaFin ? tokenDisp.fechaFin.format(DATE_TIME_FORMAT) : null,
      user: tokenDisp.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, tokenDisp.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ITokenDisp {
    return {
      ...new TokenDisp(),
      id: this.editForm.get(['id'])!.value,
      tokenConexion: this.editForm.get(['tokenConexion'])!.value,
      activo: this.editForm.get(['activo'])!.value,
      fechaInicio: this.editForm.get(['fechaInicio'])!.value
        ? dayjs(this.editForm.get(['fechaInicio'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaFin: this.editForm.get(['fechaFin'])!.value ? dayjs(this.editForm.get(['fechaFin'])!.value, DATE_TIME_FORMAT) : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
