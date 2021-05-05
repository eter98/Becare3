import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CaloriaComponent } from '../list/caloria.component';
import { CaloriaDetailComponent } from '../detail/caloria-detail.component';
import { CaloriaUpdateComponent } from '../update/caloria-update.component';
import { CaloriaRoutingResolveService } from './caloria-routing-resolve.service';

const caloriaRoute: Routes = [
  {
    path: '',
    component: CaloriaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CaloriaDetailComponent,
    resolve: {
      caloria: CaloriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CaloriaUpdateComponent,
    resolve: {
      caloria: CaloriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CaloriaUpdateComponent,
    resolve: {
      caloria: CaloriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(caloriaRoute)],
  exports: [RouterModule],
})
export class CaloriaRoutingModule {}
