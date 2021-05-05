import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPeso, Peso } from '../peso.model';
import { PesoService } from '../service/peso.service';

@Injectable({ providedIn: 'root' })
export class PesoRoutingResolveService implements Resolve<IPeso> {
  constructor(protected service: PesoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPeso> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((peso: HttpResponse<Peso>) => {
          if (peso.body) {
            return of(peso.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Peso());
  }
}
