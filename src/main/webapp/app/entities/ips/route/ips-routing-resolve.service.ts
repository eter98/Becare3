import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIPS, IPS } from '../ips.model';
import { IPSService } from '../service/ips.service';

@Injectable({ providedIn: 'root' })
export class IPSRoutingResolveService implements Resolve<IIPS> {
  constructor(protected service: IPSService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIPS> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((iPS: HttpResponse<IPS>) => {
          if (iPS.body) {
            return of(iPS.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IPS());
  }
}
