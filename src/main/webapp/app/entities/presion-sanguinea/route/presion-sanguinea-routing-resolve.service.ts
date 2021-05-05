import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPresionSanguinea, PresionSanguinea } from '../presion-sanguinea.model';
import { PresionSanguineaService } from '../service/presion-sanguinea.service';

@Injectable({ providedIn: 'root' })
export class PresionSanguineaRoutingResolveService implements Resolve<IPresionSanguinea> {
  constructor(protected service: PresionSanguineaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPresionSanguinea> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((presionSanguinea: HttpResponse<PresionSanguinea>) => {
          if (presionSanguinea.body) {
            return of(presionSanguinea.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PresionSanguinea());
  }
}
