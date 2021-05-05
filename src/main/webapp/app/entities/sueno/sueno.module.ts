import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SuenoComponent } from './list/sueno.component';
import { SuenoDetailComponent } from './detail/sueno-detail.component';
import { SuenoUpdateComponent } from './update/sueno-update.component';
import { SuenoDeleteDialogComponent } from './delete/sueno-delete-dialog.component';
import { SuenoRoutingModule } from './route/sueno-routing.module';

@NgModule({
  imports: [SharedModule, SuenoRoutingModule],
  declarations: [SuenoComponent, SuenoDetailComponent, SuenoUpdateComponent, SuenoDeleteDialogComponent],
  entryComponents: [SuenoDeleteDialogComponent],
})
export class SuenoModule {}
