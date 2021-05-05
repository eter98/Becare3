import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITratamientoMedicamento } from '../tratamiento-medicamento.model';
import { TratamientoMedicamentoService } from '../service/tratamiento-medicamento.service';

@Component({
  templateUrl: './tratamiento-medicamento-delete-dialog.component.html',
})
export class TratamientoMedicamentoDeleteDialogComponent {
  tratamientoMedicamento?: ITratamientoMedicamento;

  constructor(protected tratamientoMedicamentoService: TratamientoMedicamentoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tratamientoMedicamentoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
