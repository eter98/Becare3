import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TokenDispComponent } from '../list/token-disp.component';
import { TokenDispDetailComponent } from '../detail/token-disp-detail.component';
import { TokenDispUpdateComponent } from '../update/token-disp-update.component';
import { TokenDispRoutingResolveService } from './token-disp-routing-resolve.service';

const tokenDispRoute: Routes = [
  {
    path: '',
    component: TokenDispComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TokenDispDetailComponent,
    resolve: {
      tokenDisp: TokenDispRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TokenDispUpdateComponent,
    resolve: {
      tokenDisp: TokenDispRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TokenDispUpdateComponent,
    resolve: {
      tokenDisp: TokenDispRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tokenDispRoute)],
  exports: [RouterModule],
})
export class TokenDispRoutingModule {}
