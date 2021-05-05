import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICuestionarioEstado, CuestionarioEstado } from '../cuestionario-estado.model';
import { CuestionarioEstadoService } from '../service/cuestionario-estado.service';

@Injectable({ providedIn: 'root' })
export class CuestionarioEstadoRoutingResolveService implements Resolve<ICuestionarioEstado> {
  constructor(protected service: CuestionarioEstadoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICuestionarioEstado> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cuestionarioEstado: HttpResponse<CuestionarioEstado>) => {
          if (cuestionarioEstado.body) {
            return of(cuestionarioEstado.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CuestionarioEstado());
  }
}
