import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProgramaComponent } from '../list/programa.component';
import { ProgramaDetailComponent } from '../detail/programa-detail.component';
import { ProgramaUpdateComponent } from '../update/programa-update.component';
import { ProgramaRoutingResolveService } from './programa-routing-resolve.service';

const programaRoute: Routes = [
  {
    path: '',
    component: ProgramaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProgramaDetailComponent,
    resolve: {
      programa: ProgramaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProgramaUpdateComponent,
    resolve: {
      programa: ProgramaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProgramaUpdateComponent,
    resolve: {
      programa: ProgramaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(programaRoute)],
  exports: [RouterModule],
})
export class ProgramaRoutingModule {}
