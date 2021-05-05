import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TratamientoMedicamentoComponent } from '../list/tratamiento-medicamento.component';
import { TratamientoMedicamentoDetailComponent } from '../detail/tratamiento-medicamento-detail.component';
import { TratamientoMedicamentoUpdateComponent } from '../update/tratamiento-medicamento-update.component';
import { TratamientoMedicamentoRoutingResolveService } from './tratamiento-medicamento-routing-resolve.service';

const tratamientoMedicamentoRoute: Routes = [
  {
    path: '',
    component: TratamientoMedicamentoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TratamientoMedicamentoDetailComponent,
    resolve: {
      tratamientoMedicamento: TratamientoMedicamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TratamientoMedicamentoUpdateComponent,
    resolve: {
      tratamientoMedicamento: TratamientoMedicamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TratamientoMedicamentoUpdateComponent,
    resolve: {
      tratamientoMedicamento: TratamientoMedicamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tratamientoMedicamentoRoute)],
  exports: [RouterModule],
})
export class TratamientoMedicamentoRoutingModule {}
