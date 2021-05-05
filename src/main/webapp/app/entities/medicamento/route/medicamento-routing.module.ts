import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MedicamentoComponent } from '../list/medicamento.component';
import { MedicamentoDetailComponent } from '../detail/medicamento-detail.component';
import { MedicamentoUpdateComponent } from '../update/medicamento-update.component';
import { MedicamentoRoutingResolveService } from './medicamento-routing-resolve.service';

const medicamentoRoute: Routes = [
  {
    path: '',
    component: MedicamentoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MedicamentoDetailComponent,
    resolve: {
      medicamento: MedicamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MedicamentoUpdateComponent,
    resolve: {
      medicamento: MedicamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MedicamentoUpdateComponent,
    resolve: {
      medicamento: MedicamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(medicamentoRoute)],
  exports: [RouterModule],
})
export class MedicamentoRoutingModule {}
