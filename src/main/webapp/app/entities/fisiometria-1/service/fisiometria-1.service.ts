import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFisiometria1, getFisiometria1Identifier } from '../fisiometria-1.model';

export type EntityResponseType = HttpResponse<IFisiometria1>;
export type EntityArrayResponseType = HttpResponse<IFisiometria1[]>;

@Injectable({ providedIn: 'root' })
export class Fisiometria1Service {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/fisiometria-1-s');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(fisiometria1: IFisiometria1): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fisiometria1);
    return this.http
      .post<IFisiometria1>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fisiometria1: IFisiometria1): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fisiometria1);
    return this.http
      .put<IFisiometria1>(`${this.resourceUrl}/${getFisiometria1Identifier(fisiometria1) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fisiometria1: IFisiometria1): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fisiometria1);
    return this.http
      .patch<IFisiometria1>(`${this.resourceUrl}/${getFisiometria1Identifier(fisiometria1) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFisiometria1>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFisiometria1[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFisiometria1ToCollectionIfMissing(
    fisiometria1Collection: IFisiometria1[],
    ...fisiometria1sToCheck: (IFisiometria1 | null | undefined)[]
  ): IFisiometria1[] {
    const fisiometria1s: IFisiometria1[] = fisiometria1sToCheck.filter(isPresent);
    if (fisiometria1s.length > 0) {
      const fisiometria1CollectionIdentifiers = fisiometria1Collection.map(
        fisiometria1Item => getFisiometria1Identifier(fisiometria1Item)!
      );
      const fisiometria1sToAdd = fisiometria1s.filter(fisiometria1Item => {
        const fisiometria1Identifier = getFisiometria1Identifier(fisiometria1Item);
        if (fisiometria1Identifier == null || fisiometria1CollectionIdentifiers.includes(fisiometria1Identifier)) {
          return false;
        }
        fisiometria1CollectionIdentifiers.push(fisiometria1Identifier);
        return true;
      });
      return [...fisiometria1sToAdd, ...fisiometria1Collection];
    }
    return fisiometria1Collection;
  }

  protected convertDateFromClient(fisiometria1: IFisiometria1): IFisiometria1 {
    return Object.assign({}, fisiometria1, {
      fechaRegistro: fisiometria1.fechaRegistro?.isValid() ? fisiometria1.fechaRegistro.toJSON() : undefined,
      fechaToma: fisiometria1.fechaToma?.isValid() ? fisiometria1.fechaToma.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaRegistro = res.body.fechaRegistro ? dayjs(res.body.fechaRegistro) : undefined;
      res.body.fechaToma = res.body.fechaToma ? dayjs(res.body.fechaToma) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fisiometria1: IFisiometria1) => {
        fisiometria1.fechaRegistro = fisiometria1.fechaRegistro ? dayjs(fisiometria1.fechaRegistro) : undefined;
        fisiometria1.fechaToma = fisiometria1.fechaToma ? dayjs(fisiometria1.fechaToma) : undefined;
      });
    }
    return res;
  }
}
