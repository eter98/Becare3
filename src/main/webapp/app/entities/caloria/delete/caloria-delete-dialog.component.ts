import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICaloria } from '../caloria.model';
import { CaloriaService } from '../service/caloria.service';

@Component({
  templateUrl: './caloria-delete-dialog.component.html',
})
export class CaloriaDeleteDialogComponent {
  caloria?: ICaloria;

  constructor(protected caloriaService: CaloriaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.caloriaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
