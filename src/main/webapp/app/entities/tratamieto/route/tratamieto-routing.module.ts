import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TratamietoComponent } from '../list/tratamieto.component';
import { TratamietoDetailComponent } from '../detail/tratamieto-detail.component';
import { TratamietoUpdateComponent } from '../update/tratamieto-update.component';
import { TratamietoRoutingResolveService } from './tratamieto-routing-resolve.service';

const tratamietoRoute: Routes = [
  {
    path: '',
    component: TratamietoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TratamietoDetailComponent,
    resolve: {
      tratamieto: TratamietoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TratamietoUpdateComponent,
    resolve: {
      tratamieto: TratamietoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TratamietoUpdateComponent,
    resolve: {
      tratamieto: TratamietoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tratamietoRoute)],
  exports: [RouterModule],
})
export class TratamietoRoutingModule {}
