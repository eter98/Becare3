import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TratamietoComponent } from './list/tratamieto.component';
import { TratamietoDetailComponent } from './detail/tratamieto-detail.component';
import { TratamietoUpdateComponent } from './update/tratamieto-update.component';
import { TratamietoDeleteDialogComponent } from './delete/tratamieto-delete-dialog.component';
import { TratamietoRoutingModule } from './route/tratamieto-routing.module';

@NgModule({
  imports: [SharedModule, TratamietoRoutingModule],
  declarations: [TratamietoComponent, TratamietoDetailComponent, TratamietoUpdateComponent, TratamietoDeleteDialogComponent],
  entryComponents: [TratamietoDeleteDialogComponent],
})
export class TratamietoModule {}
