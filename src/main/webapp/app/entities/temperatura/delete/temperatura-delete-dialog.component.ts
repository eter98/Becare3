import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITemperatura } from '../temperatura.model';
import { TemperaturaService } from '../service/temperatura.service';

@Component({
  templateUrl: './temperatura-delete-dialog.component.html',
})
export class TemperaturaDeleteDialogComponent {
  temperatura?: ITemperatura;

  constructor(protected temperaturaService: TemperaturaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.temperaturaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
