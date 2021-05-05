import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIngesta } from '../ingesta.model';
import { IngestaService } from '../service/ingesta.service';

@Component({
  templateUrl: './ingesta-delete-dialog.component.html',
})
export class IngestaDeleteDialogComponent {
  ingesta?: IIngesta;

  constructor(protected ingestaService: IngestaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ingestaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
