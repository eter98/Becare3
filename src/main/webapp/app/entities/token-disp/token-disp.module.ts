import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TokenDispComponent } from './list/token-disp.component';
import { TokenDispDetailComponent } from './detail/token-disp-detail.component';
import { TokenDispUpdateComponent } from './update/token-disp-update.component';
import { TokenDispDeleteDialogComponent } from './delete/token-disp-delete-dialog.component';
import { TokenDispRoutingModule } from './route/token-disp-routing.module';

@NgModule({
  imports: [SharedModule, TokenDispRoutingModule],
  declarations: [TokenDispComponent, TokenDispDetailComponent, TokenDispUpdateComponent, TokenDispDeleteDialogComponent],
  entryComponents: [TokenDispDeleteDialogComponent],
})
export class TokenDispModule {}
