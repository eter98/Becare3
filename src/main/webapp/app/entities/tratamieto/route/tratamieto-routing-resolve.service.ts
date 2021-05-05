import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITratamieto, Tratamieto } from '../tratamieto.model';
import { TratamietoService } from '../service/tratamieto.service';

@Injectable({ providedIn: 'root' })
export class TratamietoRoutingResolveService implements Resolve<ITratamieto> {
  constructor(protected service: TratamietoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITratamieto> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tratamieto: HttpResponse<Tratamieto>) => {
          if (tratamieto.body) {
            return of(tratamieto.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tratamieto());
  }
}
