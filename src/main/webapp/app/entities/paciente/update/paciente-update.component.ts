import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPaciente, Paciente } from '../paciente.model';
import { PacienteService } from '../service/paciente.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICondicion } from 'app/entities/condicion/condicion.model';
import { CondicionService } from 'app/entities/condicion/service/condicion.service';
import { IIPS } from 'app/entities/ips/ips.model';
import { IPSService } from 'app/entities/ips/service/ips.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITratamieto } from 'app/entities/tratamieto/tratamieto.model';
import { TratamietoService } from 'app/entities/tratamieto/service/tratamieto.service';
import { IFarmaceutica } from 'app/entities/farmaceutica/farmaceutica.model';
import { FarmaceuticaService } from 'app/entities/farmaceutica/service/farmaceutica.service';

@Component({
  selector: 'jhi-paciente-update',
  templateUrl: './paciente-update.component.html',
})
export class PacienteUpdateComponent implements OnInit {
  isSaving = false;

  condicionsSharedCollection: ICondicion[] = [];
  iPSSharedCollection: IIPS[] = [];
  usersSharedCollection: IUser[] = [];
  tratamietosSharedCollection: ITratamieto[] = [];
  farmaceuticasSharedCollection: IFarmaceutica[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    tipoIdentificacion: [],
    identificacion: [],
    edad: [],
    sexo: [],
    pesoKG: [],
    estaturaCM: [],
    oximetriaReferencia: [],
    temperaturaReferencia: [],
    ritmoCardiacoReferencia: [],
    presionSistolicaReferencia: [],
    presionDistolicaReferencia: [],
    comentarios: [],
    pasosReferencia: [],
    caloriasReferencia: [],
    metaReferencia: [],
    condicion: [],
    ips: [],
    user: [],
    tratamiento: [],
    farmaceutica: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected pacienteService: PacienteService,
    protected condicionService: CondicionService,
    protected iPSService: IPSService,
    protected userService: UserService,
    protected tratamietoService: TratamietoService,
    protected farmaceuticaService: FarmaceuticaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paciente }) => {
      this.updateForm(paciente);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('becare3App.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paciente = this.createFromForm();
    if (paciente.id !== undefined) {
      this.subscribeToSaveResponse(this.pacienteService.update(paciente));
    } else {
      this.subscribeToSaveResponse(this.pacienteService.create(paciente));
    }
  }

  trackCondicionById(index: number, item: ICondicion): number {
    return item.id!;
  }

  trackIPSById(index: number, item: IIPS): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  trackTratamietoById(index: number, item: ITratamieto): number {
    return item.id!;
  }

  trackFarmaceuticaById(index: number, item: IFarmaceutica): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaciente>>): void {
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

  protected updateForm(paciente: IPaciente): void {
    this.editForm.patchValue({
      id: paciente.id,
      nombre: paciente.nombre,
      tipoIdentificacion: paciente.tipoIdentificacion,
      identificacion: paciente.identificacion,
      edad: paciente.edad,
      sexo: paciente.sexo,
      pesoKG: paciente.pesoKG,
      estaturaCM: paciente.estaturaCM,
      oximetriaReferencia: paciente.oximetriaReferencia,
      temperaturaReferencia: paciente.temperaturaReferencia,
      ritmoCardiacoReferencia: paciente.ritmoCardiacoReferencia,
      presionSistolicaReferencia: paciente.presionSistolicaReferencia,
      presionDistolicaReferencia: paciente.presionDistolicaReferencia,
      comentarios: paciente.comentarios,
      pasosReferencia: paciente.pasosReferencia,
      caloriasReferencia: paciente.caloriasReferencia,
      metaReferencia: paciente.metaReferencia,
      condicion: paciente.condicion,
      ips: paciente.ips,
      user: paciente.user,
      tratamiento: paciente.tratamiento,
      farmaceutica: paciente.farmaceutica,
    });

    this.condicionsSharedCollection = this.condicionService.addCondicionToCollectionIfMissing(
      this.condicionsSharedCollection,
      paciente.condicion
    );
    this.iPSSharedCollection = this.iPSService.addIPSToCollectionIfMissing(this.iPSSharedCollection, paciente.ips);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, paciente.user);
    this.tratamietosSharedCollection = this.tratamietoService.addTratamietoToCollectionIfMissing(
      this.tratamietosSharedCollection,
      paciente.tratamiento
    );
    this.farmaceuticasSharedCollection = this.farmaceuticaService.addFarmaceuticaToCollectionIfMissing(
      this.farmaceuticasSharedCollection,
      paciente.farmaceutica
    );
  }

  protected loadRelationshipsOptions(): void {
    this.condicionService
      .query()
      .pipe(map((res: HttpResponse<ICondicion[]>) => res.body ?? []))
      .pipe(
        map((condicions: ICondicion[]) =>
          this.condicionService.addCondicionToCollectionIfMissing(condicions, this.editForm.get('condicion')!.value)
        )
      )
      .subscribe((condicions: ICondicion[]) => (this.condicionsSharedCollection = condicions));

    this.iPSService
      .query()
      .pipe(map((res: HttpResponse<IIPS[]>) => res.body ?? []))
      .pipe(map((iPS: IIPS[]) => this.iPSService.addIPSToCollectionIfMissing(iPS, this.editForm.get('ips')!.value)))
      .subscribe((iPS: IIPS[]) => (this.iPSSharedCollection = iPS));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.tratamietoService
      .query()
      .pipe(map((res: HttpResponse<ITratamieto[]>) => res.body ?? []))
      .pipe(
        map((tratamietos: ITratamieto[]) =>
          this.tratamietoService.addTratamietoToCollectionIfMissing(tratamietos, this.editForm.get('tratamiento')!.value)
        )
      )
      .subscribe((tratamietos: ITratamieto[]) => (this.tratamietosSharedCollection = tratamietos));

    this.farmaceuticaService
      .query()
      .pipe(map((res: HttpResponse<IFarmaceutica[]>) => res.body ?? []))
      .pipe(
        map((farmaceuticas: IFarmaceutica[]) =>
          this.farmaceuticaService.addFarmaceuticaToCollectionIfMissing(farmaceuticas, this.editForm.get('farmaceutica')!.value)
        )
      )
      .subscribe((farmaceuticas: IFarmaceutica[]) => (this.farmaceuticasSharedCollection = farmaceuticas));
  }

  protected createFromForm(): IPaciente {
    return {
      ...new Paciente(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      tipoIdentificacion: this.editForm.get(['tipoIdentificacion'])!.value,
      identificacion: this.editForm.get(['identificacion'])!.value,
      edad: this.editForm.get(['edad'])!.value,
      sexo: this.editForm.get(['sexo'])!.value,
      pesoKG: this.editForm.get(['pesoKG'])!.value,
      estaturaCM: this.editForm.get(['estaturaCM'])!.value,
      oximetriaReferencia: this.editForm.get(['oximetriaReferencia'])!.value,
      temperaturaReferencia: this.editForm.get(['temperaturaReferencia'])!.value,
      ritmoCardiacoReferencia: this.editForm.get(['ritmoCardiacoReferencia'])!.value,
      presionSistolicaReferencia: this.editForm.get(['presionSistolicaReferencia'])!.value,
      presionDistolicaReferencia: this.editForm.get(['presionDistolicaReferencia'])!.value,
      comentarios: this.editForm.get(['comentarios'])!.value,
      pasosReferencia: this.editForm.get(['pasosReferencia'])!.value,
      caloriasReferencia: this.editForm.get(['caloriasReferencia'])!.value,
      metaReferencia: this.editForm.get(['metaReferencia'])!.value,
      condicion: this.editForm.get(['condicion'])!.value,
      ips: this.editForm.get(['ips'])!.value,
      user: this.editForm.get(['user'])!.value,
      tratamiento: this.editForm.get(['tratamiento'])!.value,
      farmaceutica: this.editForm.get(['farmaceutica'])!.value,
    };
  }
}
