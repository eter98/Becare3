import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { IngestaComponent } from './list/ingesta.component';
import { IngestaDetailComponent } from './detail/ingesta-detail.component';
import { IngestaUpdateComponent } from './update/ingesta-update.component';
import { IngestaDeleteDialogComponent } from './delete/ingesta-delete-dialog.component';
import { IngestaRoutingModule } from './route/ingesta-routing.module';

@NgModule({
  imports: [SharedModule, IngestaRoutingModule],
  declarations: [IngestaComponent, IngestaDetailComponent, IngestaUpdateComponent, IngestaDeleteDialogComponent],
  entryComponents: [IngestaDeleteDialogComponent],
})
export class IngestaModule {}
