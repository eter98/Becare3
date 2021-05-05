import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CuestionarioEstadoComponent } from '../list/cuestionario-estado.component';
import { CuestionarioEstadoDetailComponent } from '../detail/cuestionario-estado-detail.component';
import { CuestionarioEstadoUpdateComponent } from '../update/cuestionario-estado-update.component';
import { CuestionarioEstadoRoutingResolveService } from './cuestionario-estado-routing-resolve.service';

const cuestionarioEstadoRoute: Routes = [
  {
    path: '',
    component: CuestionarioEstadoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CuestionarioEstadoDetailComponent,
    resolve: {
      cuestionarioEstado: CuestionarioEstadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CuestionarioEstadoUpdateComponent,
    resolve: {
      cuestionarioEstado: CuestionarioEstadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CuestionarioEstadoUpdateComponent,
    resolve: {
      cuestionarioEstado: CuestionarioEstadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cuestionarioEstadoRoute)],
  exports: [RouterModule],
})
export class CuestionarioEstadoRoutingModule {}
