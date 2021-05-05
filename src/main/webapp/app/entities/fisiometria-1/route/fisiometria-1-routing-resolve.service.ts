import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFisiometria1, Fisiometria1 } from '../fisiometria-1.model';
import { Fisiometria1Service } from '../service/fisiometria-1.service';

@Injectable({ providedIn: 'root' })
export class Fisiometria1RoutingResolveService implements Resolve<IFisiometria1> {
  constructor(protected service: Fisiometria1Service, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFisiometria1> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fisiometria1: HttpResponse<Fisiometria1>) => {
          if (fisiometria1.body) {
            return of(fisiometria1.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Fisiometria1());
  }
}
