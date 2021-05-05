import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { OximetriaComponent } from './list/oximetria.component';
import { OximetriaDetailComponent } from './detail/oximetria-detail.component';
import { OximetriaUpdateComponent } from './update/oximetria-update.component';
import { OximetriaDeleteDialogComponent } from './delete/oximetria-delete-dialog.component';
import { OximetriaRoutingModule } from './route/oximetria-routing.module';

@NgModule({
  imports: [SharedModule, OximetriaRoutingModule],
  declarations: [OximetriaComponent, OximetriaDetailComponent, OximetriaUpdateComponent, OximetriaDeleteDialogComponent],
  entryComponents: [OximetriaDeleteDialogComponent],
})
export class OximetriaModule {}
