import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DispositivoComponent } from '../list/dispositivo.component';
import { DispositivoDetailComponent } from '../detail/dispositivo-detail.component';
import { DispositivoUpdateComponent } from '../update/dispositivo-update.component';
import { DispositivoRoutingResolveService } from './dispositivo-routing-resolve.service';

const dispositivoRoute: Routes = [
  {
    path: '',
    component: DispositivoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DispositivoDetailComponent,
    resolve: {
      dispositivo: DispositivoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DispositivoUpdateComponent,
    resolve: {
      dispositivo: DispositivoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DispositivoUpdateComponent,
    resolve: {
      dispositivo: DispositivoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dispositivoRoute)],
  exports: [RouterModule],
})
export class DispositivoRoutingModule {}
