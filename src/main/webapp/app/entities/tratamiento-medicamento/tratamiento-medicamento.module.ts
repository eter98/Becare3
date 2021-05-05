import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TratamientoMedicamentoComponent } from './list/tratamiento-medicamento.component';
import { TratamientoMedicamentoDetailComponent } from './detail/tratamiento-medicamento-detail.component';
import { TratamientoMedicamentoUpdateComponent } from './update/tratamiento-medicamento-update.component';
import { TratamientoMedicamentoDeleteDialogComponent } from './delete/tratamiento-medicamento-delete-dialog.component';
import { TratamientoMedicamentoRoutingModule } from './route/tratamiento-medicamento-routing.module';

@NgModule({
  imports: [SharedModule, TratamientoMedicamentoRoutingModule],
  declarations: [
    TratamientoMedicamentoComponent,
    TratamientoMedicamentoDetailComponent,
    TratamientoMedicamentoUpdateComponent,
    TratamientoMedicamentoDeleteDialogComponent,
  ],
  entryComponents: [TratamientoMedicamentoDeleteDialogComponent],
})
export class TratamientoMedicamentoModule {}
