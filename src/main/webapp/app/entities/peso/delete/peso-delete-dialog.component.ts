import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPeso } from '../peso.model';
import { PesoService } from '../service/peso.service';

@Component({
  templateUrl: './peso-delete-dialog.component.html',
})
export class PesoDeleteDialogComponent {
  peso?: IPeso;

  constructor(protected pesoService: PesoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pesoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
