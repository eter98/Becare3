import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PreguntaComponent } from '../list/pregunta.component';
import { PreguntaDetailComponent } from '../detail/pregunta-detail.component';
import { PreguntaUpdateComponent } from '../update/pregunta-update.component';
import { PreguntaRoutingResolveService } from './pregunta-routing-resolve.service';

const preguntaRoute: Routes = [
  {
    path: '',
    component: PreguntaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PreguntaDetailComponent,
    resolve: {
      pregunta: PreguntaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PreguntaUpdateComponent,
    resolve: {
      pregunta: PreguntaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PreguntaUpdateComponent,
    resolve: {
      pregunta: PreguntaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(preguntaRoute)],
  exports: [RouterModule],
})
export class PreguntaRoutingModule {}
