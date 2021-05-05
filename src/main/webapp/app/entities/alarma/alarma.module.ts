import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AlarmaComponent } from './list/alarma.component';
import { AlarmaDetailComponent } from './detail/alarma-detail.component';
import { AlarmaUpdateComponent } from './update/alarma-update.component';
import { AlarmaDeleteDialogComponent } from './delete/alarma-delete-dialog.component';
import { AlarmaRoutingModule } from './route/alarma-routing.module';

@NgModule({
  imports: [SharedModule, AlarmaRoutingModule],
  declarations: [AlarmaComponent, AlarmaDetailComponent, AlarmaUpdateComponent, AlarmaDeleteDialogComponent],
  entryComponents: [AlarmaDeleteDialogComponent],
})
export class AlarmaModule {}
