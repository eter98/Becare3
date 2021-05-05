import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AlarmaComponent } from '../list/alarma.component';
import { AlarmaDetailComponent } from '../detail/alarma-detail.component';
import { AlarmaUpdateComponent } from '../update/alarma-update.component';
import { AlarmaRoutingResolveService } from './alarma-routing-resolve.service';

const alarmaRoute: Routes = [
  {
    path: '',
    component: AlarmaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AlarmaDetailComponent,
    resolve: {
      alarma: AlarmaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AlarmaUpdateComponent,
    resolve: {
      alarma: AlarmaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AlarmaUpdateComponent,
    resolve: {
      alarma: AlarmaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(alarmaRoute)],
  exports: [RouterModule],
})
export class AlarmaRoutingModule {}
