import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISueno, Sueno } from '../sueno.model';
import { SuenoService } from '../service/sueno.service';

@Injectable({ providedIn: 'root' })
export class SuenoRoutingResolveService implements Resolve<ISueno> {
  constructor(protected service: SuenoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISueno> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sueno: HttpResponse<Sueno>) => {
          if (sueno.body) {
            return of(sueno.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Sueno());
  }
}
