import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICondicion, Condicion } from '../condicion.model';
import { CondicionService } from '../service/condicion.service';

@Injectable({ providedIn: 'root' })
export class CondicionRoutingResolveService implements Resolve<ICondicion> {
  constructor(protected service: CondicionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICondicion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((condicion: HttpResponse<Condicion>) => {
          if (condicion.body) {
            return of(condicion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Condicion());
  }
}
