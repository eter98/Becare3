import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MedicamentoComponent } from './list/medicamento.component';
import { MedicamentoDetailComponent } from './detail/medicamento-detail.component';
import { MedicamentoUpdateComponent } from './update/medicamento-update.component';
import { MedicamentoDeleteDialogComponent } from './delete/medicamento-delete-dialog.component';
import { MedicamentoRoutingModule } from './route/medicamento-routing.module';

@NgModule({
  imports: [SharedModule, MedicamentoRoutingModule],
  declarations: [MedicamentoComponent, MedicamentoDetailComponent, MedicamentoUpdateComponent, MedicamentoDeleteDialogComponent],
  entryComponents: [MedicamentoDeleteDialogComponent],
})
export class MedicamentoModule {}
