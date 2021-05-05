import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITratamieto } from '../tratamieto.model';
import { TratamietoService } from '../service/tratamieto.service';

@Component({
  templateUrl: './tratamieto-delete-dialog.component.html',
})
export class TratamietoDeleteDialogComponent {
  tratamieto?: ITratamieto;

  constructor(protected tratamietoService: TratamietoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tratamietoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
