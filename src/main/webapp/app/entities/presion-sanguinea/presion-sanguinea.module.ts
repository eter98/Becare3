import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PresionSanguineaComponent } from './list/presion-sanguinea.component';
import { PresionSanguineaDetailComponent } from './detail/presion-sanguinea-detail.component';
import { PresionSanguineaUpdateComponent } from './update/presion-sanguinea-update.component';
import { PresionSanguineaDeleteDialogComponent } from './delete/presion-sanguinea-delete-dialog.component';
import { PresionSanguineaRoutingModule } from './route/presion-sanguinea-routing.module';

@NgModule({
  imports: [SharedModule, PresionSanguineaRoutingModule],
  declarations: [
    PresionSanguineaComponent,
    PresionSanguineaDetailComponent,
    PresionSanguineaUpdateComponent,
    PresionSanguineaDeleteDialogComponent,
  ],
  entryComponents: [PresionSanguineaDeleteDialogComponent],
})
export class PresionSanguineaModule {}
