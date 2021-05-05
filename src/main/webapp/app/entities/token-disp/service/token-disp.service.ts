import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITokenDisp, getTokenDispIdentifier } from '../token-disp.model';

export type EntityResponseType = HttpResponse<ITokenDisp>;
export type EntityArrayResponseType = HttpResponse<ITokenDisp[]>;

@Injectable({ providedIn: 'root' })
export class TokenDispService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/token-disps');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(tokenDisp: ITokenDisp): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tokenDisp);
    return this.http
      .post<ITokenDisp>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tokenDisp: ITokenDisp): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tokenDisp);
    return this.http
      .put<ITokenDisp>(`${this.resourceUrl}/${getTokenDispIdentifier(tokenDisp) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tokenDisp: ITokenDisp): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tokenDisp);
    return this.http
      .patch<ITokenDisp>(`${this.resourceUrl}/${getTokenDispIdentifier(tokenDisp) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITokenDisp>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITokenDisp[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTokenDispToCollectionIfMissing(
    tokenDispCollection: ITokenDisp[],
    ...tokenDispsToCheck: (ITokenDisp | null | undefined)[]
  ): ITokenDisp[] {
    const tokenDisps: ITokenDisp[] = tokenDispsToCheck.filter(isPresent);
    if (tokenDisps.length > 0) {
      const tokenDispCollectionIdentifiers = tokenDispCollection.map(tokenDispItem => getTokenDispIdentifier(tokenDispItem)!);
      const tokenDispsToAdd = tokenDisps.filter(tokenDispItem => {
        const tokenDispIdentifier = getTokenDispIdentifier(tokenDispItem);
        if (tokenDispIdentifier == null || tokenDispCollectionIdentifiers.includes(tokenDispIdentifier)) {
          return false;
        }
        tokenDispCollectionIdentifiers.push(tokenDispIdentifier);
        return true;
      });
      return [...tokenDispsToAdd, ...tokenDispCollection];
    }
    return tokenDispCollection;
  }

  protected convertDateFromClient(tokenDisp: ITokenDisp): ITokenDisp {
    return Object.assign({}, tokenDisp, {
      fechaInicio: tokenDisp.fechaInicio?.isValid() ? tokenDisp.fechaInicio.toJSON() : undefined,
      fechaFin: tokenDisp.fechaFin?.isValid() ? tokenDisp.fechaFin.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaInicio = res.body.fechaInicio ? dayjs(res.body.fechaInicio) : undefined;
      res.body.fechaFin = res.body.fechaFin ? dayjs(res.body.fechaFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tokenDisp: ITokenDisp) => {
        tokenDisp.fechaInicio = tokenDisp.fechaInicio ? dayjs(tokenDisp.fechaInicio) : undefined;
        tokenDisp.fechaFin = tokenDisp.fechaFin ? dayjs(tokenDisp.fechaFin) : undefined;
      });
    }
    return res;
  }
}
