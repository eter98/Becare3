import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFarmaceutica } from '../farmaceutica.model';
import { FarmaceuticaService } from '../service/farmaceutica.service';

@Component({
  templateUrl: './farmaceutica-delete-dialog.component.html',
})
export class FarmaceuticaDeleteDialogComponent {
  farmaceutica?: IFarmaceutica;

  constructor(protected farmaceuticaService: FarmaceuticaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.farmaceuticaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
