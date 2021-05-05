import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDispositivo, Dispositivo } from '../dispositivo.model';
import { DispositivoService } from '../service/dispositivo.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-dispositivo-update',
  templateUrl: './dispositivo-update.component.html',
})
export class DispositivoUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    dispositivo: [],
    mac: [],
    conectado: [],
    user: [],
  });

  constructor(
    protected dispositivoService: DispositivoService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dispositivo }) => {
      this.updateForm(dispositivo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dispositivo = this.createFromForm();
    if (dispositivo.id !== undefined) {
      this.subscribeToSaveResponse(this.dispositivoService.update(dispositivo));
    } else {
      this.subscribeToSaveResponse(this.dispositivoService.create(dispositivo));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDispositivo>>): void {
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

  protected updateForm(dispositivo: IDispositivo): void {
    this.editForm.patchValue({
      id: dispositivo.id,
      dispositivo: dispositivo.dispositivo,
      mac: dispositivo.mac,
      conectado: dispositivo.conectado,
      user: dispositivo.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, dispositivo.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IDispositivo {
    return {
      ...new Dispositivo(),
      id: this.editForm.get(['id'])!.value,
      dispositivo: this.editForm.get(['dispositivo'])!.value,
      mac: this.editForm.get(['mac'])!.value,
      conectado: this.editForm.get(['conectado'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
