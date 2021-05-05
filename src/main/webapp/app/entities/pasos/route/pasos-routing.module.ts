import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PasosComponent } from '../list/pasos.component';
import { PasosDetailComponent } from '../detail/pasos-detail.component';
import { PasosUpdateComponent } from '../update/pasos-update.component';
import { PasosRoutingResolveService } from './pasos-routing-resolve.service';

const pasosRoute: Routes = [
  {
    path: '',
    component: PasosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PasosDetailComponent,
    resolve: {
      pasos: PasosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PasosUpdateComponent,
    resolve: {
      pasos: PasosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PasosUpdateComponent,
    resolve: {
      pasos: PasosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pasosRoute)],
  exports: [RouterModule],
})
export class PasosRoutingModule {}
