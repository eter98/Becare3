import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IngestaComponent } from '../list/ingesta.component';
import { IngestaDetailComponent } from '../detail/ingesta-detail.component';
import { IngestaUpdateComponent } from '../update/ingesta-update.component';
import { IngestaRoutingResolveService } from './ingesta-routing-resolve.service';

const ingestaRoute: Routes = [
  {
    path: '',
    component: IngestaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IngestaDetailComponent,
    resolve: {
      ingesta: IngestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IngestaUpdateComponent,
    resolve: {
      ingesta: IngestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IngestaUpdateComponent,
    resolve: {
      ingesta: IngestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ingestaRoute)],
  exports: [RouterModule],
})
export class IngestaRoutingModule {}
