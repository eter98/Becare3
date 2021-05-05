import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PesoComponent } from './list/peso.component';
import { PesoDetailComponent } from './detail/peso-detail.component';
import { PesoUpdateComponent } from './update/peso-update.component';
import { PesoDeleteDialogComponent } from './delete/peso-delete-dialog.component';
import { PesoRoutingModule } from './route/peso-routing.module';

@NgModule({
  imports: [SharedModule, PesoRoutingModule],
  declarations: [PesoComponent, PesoDetailComponent, PesoUpdateComponent, PesoDeleteDialogComponent],
  entryComponents: [PesoDeleteDialogComponent],
})
export class PesoModule {}
