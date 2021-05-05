import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFarmaceutica, Farmaceutica } from '../farmaceutica.model';
import { FarmaceuticaService } from '../service/farmaceutica.service';

@Injectable({ providedIn: 'root' })
export class FarmaceuticaRoutingResolveService implements Resolve<IFarmaceutica> {
  constructor(protected service: FarmaceuticaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFarmaceutica> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((farmaceutica: HttpResponse<Farmaceutica>) => {
          if (farmaceutica.body) {
            return of(farmaceutica.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Farmaceutica());
  }
}
