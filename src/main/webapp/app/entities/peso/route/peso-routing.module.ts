import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PesoComponent } from '../list/peso.component';
import { PesoDetailComponent } from '../detail/peso-detail.component';
import { PesoUpdateComponent } from '../update/peso-update.component';
import { PesoRoutingResolveService } from './peso-routing-resolve.service';

const pesoRoute: Routes = [
  {
    path: '',
    component: PesoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PesoDetailComponent,
    resolve: {
      peso: PesoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PesoUpdateComponent,
    resolve: {
      peso: PesoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PesoUpdateComponent,
    resolve: {
      peso: PesoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pesoRoute)],
  exports: [RouterModule],
})
export class PesoRoutingModule {}
