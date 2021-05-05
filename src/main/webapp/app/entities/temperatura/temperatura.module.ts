import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TemperaturaComponent } from './list/temperatura.component';
import { TemperaturaDetailComponent } from './detail/temperatura-detail.component';
import { TemperaturaUpdateComponent } from './update/temperatura-update.component';
import { TemperaturaDeleteDialogComponent } from './delete/temperatura-delete-dialog.component';
import { TemperaturaRoutingModule } from './route/temperatura-routing.module';

@NgModule({
  imports: [SharedModule, TemperaturaRoutingModule],
  declarations: [TemperaturaComponent, TemperaturaDetailComponent, TemperaturaUpdateComponent, TemperaturaDeleteDialogComponent],
  entryComponents: [TemperaturaDeleteDialogComponent],
})
export class TemperaturaModule {}
