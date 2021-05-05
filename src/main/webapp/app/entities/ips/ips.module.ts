import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { IPSComponent } from './list/ips.component';
import { IPSDetailComponent } from './detail/ips-detail.component';
import { IPSUpdateComponent } from './update/ips-update.component';
import { IPSDeleteDialogComponent } from './delete/ips-delete-dialog.component';
import { IPSRoutingModule } from './route/ips-routing.module';

@NgModule({
  imports: [SharedModule, IPSRoutingModule],
  declarations: [IPSComponent, IPSDetailComponent, IPSUpdateComponent, IPSDeleteDialogComponent],
  entryComponents: [IPSDeleteDialogComponent],
})
export class IPSModule {}
