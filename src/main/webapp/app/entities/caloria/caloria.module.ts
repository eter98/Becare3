import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CaloriaComponent } from './list/caloria.component';
import { CaloriaDetailComponent } from './detail/caloria-detail.component';
import { CaloriaUpdateComponent } from './update/caloria-update.component';
import { CaloriaDeleteDialogComponent } from './delete/caloria-delete-dialog.component';
import { CaloriaRoutingModule } from './route/caloria-routing.module';

@NgModule({
  imports: [SharedModule, CaloriaRoutingModule],
  declarations: [CaloriaComponent, CaloriaDetailComponent, CaloriaUpdateComponent, CaloriaDeleteDialogComponent],
  entryComponents: [CaloriaDeleteDialogComponent],
})
export class CaloriaModule {}
