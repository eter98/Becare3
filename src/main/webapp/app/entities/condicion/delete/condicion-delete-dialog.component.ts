import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICondicion } from '../condicion.model';
import { CondicionService } from '../service/condicion.service';

@Component({
  templateUrl: './condicion-delete-dialog.component.html',
})
export class CondicionDeleteDialogComponent {
  condicion?: ICondicion;

  constructor(protected condicionService: CondicionService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.condicionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
