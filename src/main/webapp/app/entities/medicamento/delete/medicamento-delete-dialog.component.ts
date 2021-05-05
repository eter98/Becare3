import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMedicamento } from '../medicamento.model';
import { MedicamentoService } from '../service/medicamento.service';

@Component({
  templateUrl: './medicamento-delete-dialog.component.html',
})
export class MedicamentoDeleteDialogComponent {
  medicamento?: IMedicamento;

  constructor(protected medicamentoService: MedicamentoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicamentoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
