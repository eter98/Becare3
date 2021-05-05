import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AdherenciaComponent } from '../list/adherencia.component';
import { AdherenciaDetailComponent } from '../detail/adherencia-detail.component';
import { AdherenciaUpdateComponent } from '../update/adherencia-update.component';
import { AdherenciaRoutingResolveService } from './adherencia-routing-resolve.service';

const adherenciaRoute: Routes = [
  {
    path: '',
    component: AdherenciaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AdherenciaDetailComponent,
    resolve: {
      adherencia: AdherenciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AdherenciaUpdateComponent,
    resolve: {
      adherencia: AdherenciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AdherenciaUpdateComponent,
    resolve: {
      adherencia: AdherenciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(adherenciaRoute)],
  exports: [RouterModule],
})
export class AdherenciaRoutingModule {}
