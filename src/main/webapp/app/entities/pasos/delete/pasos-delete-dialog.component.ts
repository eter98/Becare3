import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPasos } from '../pasos.model';
import { PasosService } from '../service/pasos.service';

@Component({
  templateUrl: './pasos-delete-dialog.component.html',
})
export class PasosDeleteDialogComponent {
  pasos?: IPasos;

  constructor(protected pasosService: PasosService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pasosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
