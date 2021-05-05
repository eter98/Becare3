import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAlarma } from '../alarma.model';
import { AlarmaService } from '../service/alarma.service';

@Component({
  templateUrl: './alarma-delete-dialog.component.html',
})
export class AlarmaDeleteDialogComponent {
  alarma?: IAlarma;

  constructor(protected alarmaService: AlarmaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.alarmaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
