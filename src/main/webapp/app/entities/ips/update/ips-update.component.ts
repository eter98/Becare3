import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IIPS, IPS } from '../ips.model';
import { IPSService } from '../service/ips.service';

@Component({
  selector: 'jhi-ips-update',
  templateUrl: './ips-update.component.html',
})
export class IPSUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    nit: [],
    direccion: [],
    telefono: [],
    correoElectronico: [],
  });

  constructor(protected iPSService: IPSService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ iPS }) => {
      this.updateForm(iPS);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const iPS = this.createFromForm();
    if (iPS.id !== undefined) {
      this.subscribeToSaveResponse(this.iPSService.update(iPS));
    } else {
      this.subscribeToSaveResponse(this.iPSService.create(iPS));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIPS>>): void {
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

  protected updateForm(iPS: IIPS): void {
    this.editForm.patchValue({
      id: iPS.id,
      nombre: iPS.nombre,
      nit: iPS.nit,
      direccion: iPS.direccion,
      telefono: iPS.telefono,
      correoElectronico: iPS.correoElectronico,
    });
  }

  protected createFromForm(): IIPS {
    return {
      ...new IPS(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      nit: this.editForm.get(['nit'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      correoElectronico: this.editForm.get(['correoElectronico'])!.value,
    };
  }
}
