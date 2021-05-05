import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PreguntaComponent } from './list/pregunta.component';
import { PreguntaDetailComponent } from './detail/pregunta-detail.component';
import { PreguntaUpdateComponent } from './update/pregunta-update.component';
import { PreguntaDeleteDialogComponent } from './delete/pregunta-delete-dialog.component';
import { PreguntaRoutingModule } from './route/pregunta-routing.module';

@NgModule({
  imports: [SharedModule, PreguntaRoutingModule],
  declarations: [PreguntaComponent, PreguntaDetailComponent, PreguntaUpdateComponent, PreguntaDeleteDialogComponent],
  entryComponents: [PreguntaDeleteDialogComponent],
})
export class PreguntaModule {}
