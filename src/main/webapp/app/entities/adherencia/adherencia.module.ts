import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AdherenciaComponent } from './list/adherencia.component';
import { AdherenciaDetailComponent } from './detail/adherencia-detail.component';
import { AdherenciaUpdateComponent } from './update/adherencia-update.component';
import { AdherenciaDeleteDialogComponent } from './delete/adherencia-delete-dialog.component';
import { AdherenciaRoutingModule } from './route/adherencia-routing.module';

@NgModule({
  imports: [SharedModule, AdherenciaRoutingModule],
  declarations: [AdherenciaComponent, AdherenciaDetailComponent, AdherenciaUpdateComponent, AdherenciaDeleteDialogComponent],
  entryComponents: [AdherenciaDeleteDialogComponent],
})
export class AdherenciaModule {}
