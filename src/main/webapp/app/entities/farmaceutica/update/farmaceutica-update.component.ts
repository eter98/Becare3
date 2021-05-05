import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFarmaceutica, Farmaceutica } from '../farmaceutica.model';
import { FarmaceuticaService } from '../service/farmaceutica.service';

@Component({
  selector: 'jhi-farmaceutica-update',
  templateUrl: './farmaceutica-update.component.html',
})
export class FarmaceuticaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    direccion: [],
    propietario: [],
  });

  constructor(protected farmaceuticaService: FarmaceuticaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ farmaceutica }) => {
      this.updateForm(farmaceutica);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const farmaceutica = this.createFromForm();
    if (farmaceutica.id !== undefined) {
      this.subscribeToSaveResponse(this.farmaceuticaService.update(farmaceutica));
    } else {
      this.subscribeToSaveResponse(this.farmaceuticaService.create(farmaceutica));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFarmaceutica>>): void {
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

  protected updateForm(farmaceutica: IFarmaceutica): void {
    this.editForm.patchValue({
      id: farmaceutica.id,
      nombre: farmaceutica.nombre,
      direccion: farmaceutica.direccion,
      propietario: farmaceutica.propietario,
    });
  }

  protected createFromForm(): IFarmaceutica {
    return {
      ...new Farmaceutica(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      propietario: this.editForm.get(['propietario'])!.value,
    };
  }
}
