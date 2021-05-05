import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SuenoComponent } from '../list/sueno.component';
import { SuenoDetailComponent } from '../detail/sueno-detail.component';
import { SuenoUpdateComponent } from '../update/sueno-update.component';
import { SuenoRoutingResolveService } from './sueno-routing-resolve.service';

const suenoRoute: Routes = [
  {
    path: '',
    component: SuenoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SuenoDetailComponent,
    resolve: {
      sueno: SuenoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SuenoUpdateComponent,
    resolve: {
      sueno: SuenoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SuenoUpdateComponent,
    resolve: {
      sueno: SuenoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(suenoRoute)],
  exports: [RouterModule],
})
export class SuenoRoutingModule {}
