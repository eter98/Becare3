import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPregunta } from '../pregunta.model';
import { PreguntaService } from '../service/pregunta.service';

@Component({
  templateUrl: './pregunta-delete-dialog.component.html',
})
export class PreguntaDeleteDialogComponent {
  pregunta?: IPregunta;

  constructor(protected preguntaService: PreguntaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.preguntaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
