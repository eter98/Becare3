import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Fisiometria1Component } from '../list/fisiometria-1.component';
import { Fisiometria1DetailComponent } from '../detail/fisiometria-1-detail.component';
import { Fisiometria1UpdateComponent } from '../update/fisiometria-1-update.component';
import { Fisiometria1RoutingResolveService } from './fisiometria-1-routing-resolve.service';

const fisiometria1Route: Routes = [
  {
    path: '',
    component: Fisiometria1Component,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: Fisiometria1DetailComponent,
    resolve: {
      fisiometria1: Fisiometria1RoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: Fisiometria1UpdateComponent,
    resolve: {
      fisiometria1: Fisiometria1RoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: Fisiometria1UpdateComponent,
    resolve: {
      fisiometria1: Fisiometria1RoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fisiometria1Route)],
  exports: [RouterModule],
})
export class Fisiometria1RoutingModule {}
