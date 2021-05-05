import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlarma, Alarma } from '../alarma.model';
import { AlarmaService } from '../service/alarma.service';

@Injectable({ providedIn: 'root' })
export class AlarmaRoutingResolveService implements Resolve<IAlarma> {
  constructor(protected service: AlarmaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAlarma> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((alarma: HttpResponse<Alarma>) => {
          if (alarma.body) {
            return of(alarma.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Alarma());
  }
}
