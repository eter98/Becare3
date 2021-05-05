import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOximetria, Oximetria } from '../oximetria.model';
import { OximetriaService } from '../service/oximetria.service';

@Injectable({ providedIn: 'root' })
export class OximetriaRoutingResolveService implements Resolve<IOximetria> {
  constructor(protected service: OximetriaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOximetria> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((oximetria: HttpResponse<Oximetria>) => {
          if (oximetria.body) {
            return of(oximetria.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Oximetria());
  }
}
