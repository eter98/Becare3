import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITokenDisp, TokenDisp } from '../token-disp.model';
import { TokenDispService } from '../service/token-disp.service';

@Injectable({ providedIn: 'root' })
export class TokenDispRoutingResolveService implements Resolve<ITokenDisp> {
  constructor(protected service: TokenDispService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITokenDisp> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tokenDisp: HttpResponse<TokenDisp>) => {
          if (tokenDisp.body) {
            return of(tokenDisp.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TokenDisp());
  }
}
