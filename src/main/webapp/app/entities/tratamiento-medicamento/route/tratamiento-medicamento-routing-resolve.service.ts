import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITratamientoMedicamento, TratamientoMedicamento } from '../tratamiento-medicamento.model';
import { TratamientoMedicamentoService } from '../service/tratamiento-medicamento.service';

@Injectable({ providedIn: 'root' })
export class TratamientoMedicamentoRoutingResolveService implements Resolve<ITratamientoMedicamento> {
  constructor(protected service: TratamientoMedicamentoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITratamientoMedicamento> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tratamientoMedicamento: HttpResponse<TratamientoMedicamento>) => {
          if (tratamientoMedicamento.body) {
            return of(tratamientoMedicamento.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TratamientoMedicamento());
  }
}
