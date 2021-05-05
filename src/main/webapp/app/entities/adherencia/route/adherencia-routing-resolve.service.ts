import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdherencia, Adherencia } from '../adherencia.model';
import { AdherenciaService } from '../service/adherencia.service';

@Injectable({ providedIn: 'root' })
export class AdherenciaRoutingResolveService implements Resolve<IAdherencia> {
  constructor(protected service: AdherenciaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAdherencia> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((adherencia: HttpResponse<Adherencia>) => {
          if (adherencia.body) {
            return of(adherencia.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Adherencia());
  }
}
