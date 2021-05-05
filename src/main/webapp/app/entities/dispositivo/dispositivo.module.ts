import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DispositivoComponent } from './list/dispositivo.component';
import { DispositivoDetailComponent } from './detail/dispositivo-detail.component';
import { DispositivoUpdateComponent } from './update/dispositivo-update.component';
import { DispositivoDeleteDialogComponent } from './delete/dispositivo-delete-dialog.component';
import { DispositivoRoutingModule } from './route/dispositivo-routing.module';

@NgModule({
  imports: [SharedModule, DispositivoRoutingModule],
  declarations: [DispositivoComponent, DispositivoDetailComponent, DispositivoUpdateComponent, DispositivoDeleteDialogComponent],
  entryComponents: [DispositivoDeleteDialogComponent],
})
export class DispositivoModule {}
