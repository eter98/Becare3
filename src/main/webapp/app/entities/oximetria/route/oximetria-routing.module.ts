import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OximetriaComponent } from '../list/oximetria.component';
import { OximetriaDetailComponent } from '../detail/oximetria-detail.component';
import { OximetriaUpdateComponent } from '../update/oximetria-update.component';
import { OximetriaRoutingResolveService } from './oximetria-routing-resolve.service';

const oximetriaRoute: Routes = [
  {
    path: '',
    component: OximetriaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OximetriaDetailComponent,
    resolve: {
      oximetria: OximetriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OximetriaUpdateComponent,
    resolve: {
      oximetria: OximetriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OximetriaUpdateComponent,
    resolve: {
      oximetria: OximetriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(oximetriaRoute)],
  exports: [RouterModule],
})
export class OximetriaRoutingModule {}
