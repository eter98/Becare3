import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFisiometria1 } from '../fisiometria-1.model';
import { Fisiometria1Service } from '../service/fisiometria-1.service';

@Component({
  templateUrl: './fisiometria-1-delete-dialog.component.html',
})
export class Fisiometria1DeleteDialogComponent {
  fisiometria1?: IFisiometria1;

  constructor(protected fisiometria1Service: Fisiometria1Service, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fisiometria1Service.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
