import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdherencia } from '../adherencia.model';
import { AdherenciaService } from '../service/adherencia.service';

@Component({
  templateUrl: './adherencia-delete-dialog.component.html',
})
export class AdherenciaDeleteDialogComponent {
  adherencia?: IAdherencia;

  constructor(protected adherenciaService: AdherenciaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.adherenciaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
