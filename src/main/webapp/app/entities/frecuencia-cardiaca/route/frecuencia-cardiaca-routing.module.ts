import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FrecuenciaCardiacaComponent } from '../list/frecuencia-cardiaca.component';
import { FrecuenciaCardiacaDetailComponent } from '../detail/frecuencia-cardiaca-detail.component';
import { FrecuenciaCardiacaUpdateComponent } from '../update/frecuencia-cardiaca-update.component';
import { FrecuenciaCardiacaRoutingResolveService } from './frecuencia-cardiaca-routing-resolve.service';

const frecuenciaCardiacaRoute: Routes = [
  {
    path: '',
    component: FrecuenciaCardiacaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FrecuenciaCardiacaDetailComponent,
    resolve: {
      frecuenciaCardiaca: FrecuenciaCardiacaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FrecuenciaCardiacaUpdateComponent,
    resolve: {
      frecuenciaCardiaca: FrecuenciaCardiacaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FrecuenciaCardiacaUpdateComponent,
    resolve: {
      frecuenciaCardiaca: FrecuenciaCardiacaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(frecuenciaCardiacaRoute)],
  exports: [RouterModule],
})
export class FrecuenciaCardiacaRoutingModule {}
