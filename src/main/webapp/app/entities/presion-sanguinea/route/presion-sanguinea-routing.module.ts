import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PresionSanguineaComponent } from '../list/presion-sanguinea.component';
import { PresionSanguineaDetailComponent } from '../detail/presion-sanguinea-detail.component';
import { PresionSanguineaUpdateComponent } from '../update/presion-sanguinea-update.component';
import { PresionSanguineaRoutingResolveService } from './presion-sanguinea-routing-resolve.service';

const presionSanguineaRoute: Routes = [
  {
    path: '',
    component: PresionSanguineaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PresionSanguineaDetailComponent,
    resolve: {
      presionSanguinea: PresionSanguineaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PresionSanguineaUpdateComponent,
    resolve: {
      presionSanguinea: PresionSanguineaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PresionSanguineaUpdateComponent,
    resolve: {
      presionSanguinea: PresionSanguineaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(presionSanguineaRoute)],
  exports: [RouterModule],
})
export class PresionSanguineaRoutingModule {}
