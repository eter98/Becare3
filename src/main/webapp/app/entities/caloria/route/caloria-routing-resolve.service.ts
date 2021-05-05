import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICaloria, Caloria } from '../caloria.model';
import { CaloriaService } from '../service/caloria.service';

@Injectable({ providedIn: 'root' })
export class CaloriaRoutingResolveService implements Resolve<ICaloria> {
  constructor(protected service: CaloriaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICaloria> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((caloria: HttpResponse<Caloria>) => {
          if (caloria.body) {
            return of(caloria.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Caloria());
  }
}
