import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPregunta, Pregunta } from '../pregunta.model';
import { PreguntaService } from '../service/pregunta.service';

@Injectable({ providedIn: 'root' })
export class PreguntaRoutingResolveService implements Resolve<IPregunta> {
  constructor(protected service: PreguntaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPregunta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pregunta: HttpResponse<Pregunta>) => {
          if (pregunta.body) {
            return of(pregunta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Pregunta());
  }
}
