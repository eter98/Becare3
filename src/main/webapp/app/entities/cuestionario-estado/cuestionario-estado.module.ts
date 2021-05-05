import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CuestionarioEstadoComponent } from './list/cuestionario-estado.component';
import { CuestionarioEstadoDetailComponent } from './detail/cuestionario-estado-detail.component';
import { CuestionarioEstadoUpdateComponent } from './update/cuestionario-estado-update.component';
import { CuestionarioEstadoDeleteDialogComponent } from './delete/cuestionario-estado-delete-dialog.component';
import { CuestionarioEstadoRoutingModule } from './route/cuestionario-estado-routing.module';

@NgModule({
  imports: [SharedModule, CuestionarioEstadoRoutingModule],
  declarations: [
    CuestionarioEstadoComponent,
    CuestionarioEstadoDetailComponent,
    CuestionarioEstadoUpdateComponent,
    CuestionarioEstadoDeleteDialogComponent,
  ],
  entryComponents: [CuestionarioEstadoDeleteDialogComponent],
})
export class CuestionarioEstadoModule {}
