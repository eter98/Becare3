import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPresionSanguinea } from '../presion-sanguinea.model';
import { PresionSanguineaService } from '../service/presion-sanguinea.service';

@Component({
  templateUrl: './presion-sanguinea-delete-dialog.component.html',
})
export class PresionSanguineaDeleteDialogComponent {
  presionSanguinea?: IPresionSanguinea;

  constructor(protected presionSanguineaService: PresionSanguineaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.presionSanguineaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
