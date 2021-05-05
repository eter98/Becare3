import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICuestionarioEstado } from '../cuestionario-estado.model';
import { CuestionarioEstadoService } from '../service/cuestionario-estado.service';

@Component({
  templateUrl: './cuestionario-estado-delete-dialog.component.html',
})
export class CuestionarioEstadoDeleteDialogComponent {
  cuestionarioEstado?: ICuestionarioEstado;

  constructor(protected cuestionarioEstadoService: CuestionarioEstadoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cuestionarioEstadoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
