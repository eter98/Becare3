import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIngesta, Ingesta } from '../ingesta.model';
import { IngestaService } from '../service/ingesta.service';

@Injectable({ providedIn: 'root' })
export class IngestaRoutingResolveService implements Resolve<IIngesta> {
  constructor(protected service: IngestaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIngesta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ingesta: HttpResponse<Ingesta>) => {
          if (ingesta.body) {
            return of(ingesta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ingesta());
  }
}
