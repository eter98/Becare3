import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITokenDisp } from '../token-disp.model';
import { TokenDispService } from '../service/token-disp.service';

@Component({
  templateUrl: './token-disp-delete-dialog.component.html',
})
export class TokenDispDeleteDialogComponent {
  tokenDisp?: ITokenDisp;

  constructor(protected tokenDispService: TokenDispService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tokenDispService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
