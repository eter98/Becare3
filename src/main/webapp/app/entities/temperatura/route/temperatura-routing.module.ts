import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TemperaturaComponent } from '../list/temperatura.component';
import { TemperaturaDetailComponent } from '../detail/temperatura-detail.component';
import { TemperaturaUpdateComponent } from '../update/temperatura-update.component';
import { TemperaturaRoutingResolveService } from './temperatura-routing-resolve.service';

const temperaturaRoute: Routes = [
  {
    path: '',
    component: TemperaturaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TemperaturaDetailComponent,
    resolve: {
      temperatura: TemperaturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TemperaturaUpdateComponent,
    resolve: {
      temperatura: TemperaturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TemperaturaUpdateComponent,
    resolve: {
      temperatura: TemperaturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(temperaturaRoute)],
  exports: [RouterModule],
})
export class TemperaturaRoutingModule {}
