import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FrecuenciaCardiacaComponent } from './list/frecuencia-cardiaca.component';
import { FrecuenciaCardiacaDetailComponent } from './detail/frecuencia-cardiaca-detail.component';
import { FrecuenciaCardiacaUpdateComponent } from './update/frecuencia-cardiaca-update.component';
import { FrecuenciaCardiacaDeleteDialogComponent } from './delete/frecuencia-cardiaca-delete-dialog.component';
import { FrecuenciaCardiacaRoutingModule } from './route/frecuencia-cardiaca-routing.module';

@NgModule({
  imports: [SharedModule, FrecuenciaCardiacaRoutingModule],
  declarations: [
    FrecuenciaCardiacaComponent,
    FrecuenciaCardiacaDetailComponent,
    FrecuenciaCardiacaUpdateComponent,
    FrecuenciaCardiacaDeleteDialogComponent,
  ],
  entryComponents: [FrecuenciaCardiacaDeleteDialogComponent],
})
export class FrecuenciaCardiacaModule {}
