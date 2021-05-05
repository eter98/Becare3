import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIPS } from '../ips.model';
import { IPSService } from '../service/ips.service';

@Component({
  templateUrl: './ips-delete-dialog.component.html',
})
export class IPSDeleteDialogComponent {
  iPS?: IIPS;

  constructor(protected iPSService: IPSService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.iPSService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
