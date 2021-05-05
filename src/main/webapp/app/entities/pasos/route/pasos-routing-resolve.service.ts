import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPasos, Pasos } from '../pasos.model';
import { PasosService } from '../service/pasos.service';

@Injectable({ providedIn: 'root' })
export class PasosRoutingResolveService implements Resolve<IPasos> {
  constructor(protected service: PasosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPasos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pasos: HttpResponse<Pasos>) => {
          if (pasos.body) {
            return of(pasos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Pasos());
  }
}
