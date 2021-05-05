import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IPSComponent } from '../list/ips.component';
import { IPSDetailComponent } from '../detail/ips-detail.component';
import { IPSUpdateComponent } from '../update/ips-update.component';
import { IPSRoutingResolveService } from './ips-routing-resolve.service';

const iPSRoute: Routes = [
  {
    path: '',
    component: IPSComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IPSDetailComponent,
    resolve: {
      iPS: IPSRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IPSUpdateComponent,
    resolve: {
      iPS: IPSRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IPSUpdateComponent,
    resolve: {
      iPS: IPSRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(iPSRoute)],
  exports: [RouterModule],
})
export class IPSRoutingModule {}
