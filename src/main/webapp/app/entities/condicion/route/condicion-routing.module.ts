import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CondicionComponent } from '../list/condicion.component';
import { CondicionDetailComponent } from '../detail/condicion-detail.component';
import { CondicionUpdateComponent } from '../update/condicion-update.component';
import { CondicionRoutingResolveService } from './condicion-routing-resolve.service';

const condicionRoute: Routes = [
  {
    path: '',
    component: CondicionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CondicionDetailComponent,
    resolve: {
      condicion: CondicionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CondicionUpdateComponent,
    resolve: {
      condicion: CondicionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CondicionUpdateComponent,
    resolve: {
      condicion: CondicionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(condicionRoute)],
  exports: [RouterModule],
})
export class CondicionRoutingModule {}
