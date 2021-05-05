import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrograma, Programa } from '../programa.model';
import { ProgramaService } from '../service/programa.service';

@Injectable({ providedIn: 'root' })
export class ProgramaRoutingResolveService implements Resolve<IPrograma> {
  constructor(protected service: ProgramaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrograma> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((programa: HttpResponse<Programa>) => {
          if (programa.body) {
            return of(programa.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Programa());
  }
}
