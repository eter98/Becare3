import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FarmaceuticaComponent } from '../list/farmaceutica.component';
import { FarmaceuticaDetailComponent } from '../detail/farmaceutica-detail.component';
import { FarmaceuticaUpdateComponent } from '../update/farmaceutica-update.component';
import { FarmaceuticaRoutingResolveService } from './farmaceutica-routing-resolve.service';

const farmaceuticaRoute: Routes = [
  {
    path: '',
    component: FarmaceuticaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FarmaceuticaDetailComponent,
    resolve: {
      farmaceutica: FarmaceuticaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FarmaceuticaUpdateComponent,
    resolve: {
      farmaceutica: FarmaceuticaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FarmaceuticaUpdateComponent,
    resolve: {
      farmaceutica: FarmaceuticaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(farmaceuticaRoute)],
  exports: [RouterModule],
})
export class FarmaceuticaRoutingModule {}
