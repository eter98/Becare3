import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FarmaceuticaComponent } from './list/farmaceutica.component';
import { FarmaceuticaDetailComponent } from './detail/farmaceutica-detail.component';
import { FarmaceuticaUpdateComponent } from './update/farmaceutica-update.component';
import { FarmaceuticaDeleteDialogComponent } from './delete/farmaceutica-delete-dialog.component';
import { FarmaceuticaRoutingModule } from './route/farmaceutica-routing.module';

@NgModule({
  imports: [SharedModule, FarmaceuticaRoutingModule],
  declarations: [FarmaceuticaComponent, FarmaceuticaDetailComponent, FarmaceuticaUpdateComponent, FarmaceuticaDeleteDialogComponent],
  entryComponents: [FarmaceuticaDeleteDialogComponent],
})
export class FarmaceuticaModule {}
