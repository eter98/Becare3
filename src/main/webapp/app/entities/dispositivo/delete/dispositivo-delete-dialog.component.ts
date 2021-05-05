import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDispositivo } from '../dispositivo.model';
import { DispositivoService } from '../service/dispositivo.service';

@Component({
  templateUrl: './dispositivo-delete-dialog.component.html',
})
export class DispositivoDeleteDialogComponent {
  dispositivo?: IDispositivo;

  constructor(protected dispositivoService: DispositivoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dispositivoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
