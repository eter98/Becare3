import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrograma } from '../programa.model';
import { ProgramaService } from '../service/programa.service';

@Component({
  templateUrl: './programa-delete-dialog.component.html',
})
export class ProgramaDeleteDialogComponent {
  programa?: IPrograma;

  constructor(protected programaService: ProgramaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.programaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
