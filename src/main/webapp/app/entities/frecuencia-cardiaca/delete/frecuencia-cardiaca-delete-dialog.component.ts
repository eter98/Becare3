import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFrecuenciaCardiaca } from '../frecuencia-cardiaca.model';
import { FrecuenciaCardiacaService } from '../service/frecuencia-cardiaca.service';

@Component({
  templateUrl: './frecuencia-cardiaca-delete-dialog.component.html',
})
export class FrecuenciaCardiacaDeleteDialogComponent {
  frecuenciaCardiaca?: IFrecuenciaCardiaca;

  constructor(protected frecuenciaCardiacaService: FrecuenciaCardiacaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.frecuenciaCardiacaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
