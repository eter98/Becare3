import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { Fisiometria1Component } from './list/fisiometria-1.component';
import { Fisiometria1DetailComponent } from './detail/fisiometria-1-detail.component';
import { Fisiometria1UpdateComponent } from './update/fisiometria-1-update.component';
import { Fisiometria1DeleteDialogComponent } from './delete/fisiometria-1-delete-dialog.component';
import { Fisiometria1RoutingModule } from './route/fisiometria-1-routing.module';

@NgModule({
  imports: [SharedModule, Fisiometria1RoutingModule],
  declarations: [Fisiometria1Component, Fisiometria1DetailComponent, Fisiometria1UpdateComponent, Fisiometria1DeleteDialogComponent],
  entryComponents: [Fisiometria1DeleteDialogComponent],
})
export class Fisiometria1Module {}
