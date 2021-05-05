import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISueno } from '../sueno.model';
import { SuenoService } from '../service/sueno.service';

@Component({
  templateUrl: './sueno-delete-dialog.component.html',
})
export class SuenoDeleteDialogComponent {
  sueno?: ISueno;

  constructor(protected suenoService: SuenoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.suenoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
