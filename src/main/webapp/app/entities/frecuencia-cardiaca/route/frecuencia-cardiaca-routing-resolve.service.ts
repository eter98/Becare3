import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFrecuenciaCardiaca, FrecuenciaCardiaca } from '../frecuencia-cardiaca.model';
import { FrecuenciaCardiacaService } from '../service/frecuencia-cardiaca.service';

@Injectable({ providedIn: 'root' })
export class FrecuenciaCardiacaRoutingResolveService implements Resolve<IFrecuenciaCardiaca> {
  constructor(protected service: FrecuenciaCardiacaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFrecuenciaCardiaca> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((frecuenciaCardiaca: HttpResponse<FrecuenciaCardiaca>) => {
          if (frecuenciaCardiaca.body) {
            return of(frecuenciaCardiaca.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FrecuenciaCardiaca());
  }
}
