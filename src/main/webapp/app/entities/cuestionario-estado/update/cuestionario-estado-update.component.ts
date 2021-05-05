import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICuestionarioEstado, CuestionarioEstado } from '../cuestionario-estado.model';
import { CuestionarioEstadoService } from '../service/cuestionario-estado.service';
import { IPregunta } from 'app/entities/pregunta/pregunta.model';
import { PreguntaService } from 'app/entities/pregunta/service/pregunta.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-cuestionario-estado-update',
  templateUrl: './cuestionario-estado-update.component.html',
})
export class CuestionarioEstadoUpdateComponent implements OnInit {
  isSaving = false;

  preguntasSharedCollection: IPregunta[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    valor: [],
    valoracion: [],
    pregunta: [],
    user: [],
  });

  constructor(
    protected cuestionarioEstadoService: CuestionarioEstadoService,
    protected preguntaService: PreguntaService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cuestionarioEstado }) => {
      this.updateForm(cuestionarioEstado);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cuestionarioEstado = this.createFromForm();
    if (cuestionarioEstado.id !== undefined) {
      this.subscribeToSaveResponse(this.cuestionarioEstadoService.update(cuestionarioEstado));
    } else {
      this.subscribeToSaveResponse(this.cuestionarioEstadoService.create(cuestionarioEstado));
    }
  }

  trackPreguntaById(index: number, item: IPregunta): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICuestionarioEstado>>): void {
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

  protected updateForm(cuestionarioEstado: ICuestionarioEstado): void {
    this.editForm.patchValue({
      id: cuestionarioEstado.id,
      valor: cuestionarioEstado.valor,
      valoracion: cuestionarioEstado.valoracion,
      pregunta: cuestionarioEstado.pregunta,
      user: cuestionarioEstado.user,
    });

    this.preguntasSharedCollection = this.preguntaService.addPreguntaToCollectionIfMissing(
      this.preguntasSharedCollection,
      cuestionarioEstado.pregunta
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, cuestionarioEstado.user);
  }

  protected loadRelationshipsOptions(): void {
    this.preguntaService
      .query()
      .pipe(map((res: HttpResponse<IPregunta[]>) => res.body ?? []))
      .pipe(
        map((preguntas: IPregunta[]) =>
          this.preguntaService.addPreguntaToCollectionIfMissing(preguntas, this.editForm.get('pregunta')!.value)
        )
      )
      .subscribe((preguntas: IPregunta[]) => (this.preguntasSharedCollection = preguntas));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ICuestionarioEstado {
    return {
      ...new CuestionarioEstado(),
      id: this.editForm.get(['id'])!.value,
      valor: this.editForm.get(['valor'])!.value,
      valoracion: this.editForm.get(['valoracion'])!.value,
      pregunta: this.editForm.get(['pregunta'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
