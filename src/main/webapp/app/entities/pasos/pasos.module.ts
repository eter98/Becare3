import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PasosComponent } from './list/pasos.component';
import { PasosDetailComponent } from './detail/pasos-detail.component';
import { PasosUpdateComponent } from './update/pasos-update.component';
import { PasosDeleteDialogComponent } from './delete/pasos-delete-dialog.component';
import { PasosRoutingModule } from './route/pasos-routing.module';

@NgModule({
  imports: [SharedModule, PasosRoutingModule],
  declarations: [PasosComponent, PasosDetailComponent, PasosUpdateComponent, PasosDeleteDialogComponent],
  entryComponents: [PasosDeleteDialogComponent],
})
export class PasosModule {}
