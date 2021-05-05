import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CondicionComponent } from './list/condicion.component';
import { CondicionDetailComponent } from './detail/condicion-detail.component';
import { CondicionUpdateComponent } from './update/condicion-update.component';
import { CondicionDeleteDialogComponent } from './delete/condicion-delete-dialog.component';
import { CondicionRoutingModule } from './route/condicion-routing.module';

@NgModule({
  imports: [SharedModule, CondicionRoutingModule],
  declarations: [CondicionComponent, CondicionDetailComponent, CondicionUpdateComponent, CondicionDeleteDialogComponent],
  entryComponents: [CondicionDeleteDialogComponent],
})
export class CondicionModule {}
