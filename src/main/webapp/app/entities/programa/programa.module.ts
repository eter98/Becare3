import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ProgramaComponent } from './list/programa.component';
import { ProgramaDetailComponent } from './detail/programa-detail.component';
import { ProgramaUpdateComponent } from './update/programa-update.component';
import { ProgramaDeleteDialogComponent } from './delete/programa-delete-dialog.component';
import { ProgramaRoutingModule } from './route/programa-routing.module';

@NgModule({
  imports: [SharedModule, ProgramaRoutingModule],
  declarations: [ProgramaComponent, ProgramaDetailComponent, ProgramaUpdateComponent, ProgramaDeleteDialogComponent],
  entryComponents: [ProgramaDeleteDialogComponent],
})
export class ProgramaModule {}
