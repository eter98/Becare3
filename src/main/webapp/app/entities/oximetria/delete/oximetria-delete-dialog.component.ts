import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOximetria } from '../oximetria.model';
import { OximetriaService } from '../service/oximetria.service';

@Component({
  templateUrl: './oximetria-delete-dialog.component.html',
})
export class OximetriaDeleteDialogComponent {
  oximetria?: IOximetria;

  constructor(protected oximetriaService: OximetriaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.oximetriaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
