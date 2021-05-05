import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdherencia, getAdherenciaIdentifier } from '../adherencia.model';

export type EntityResponseType = HttpResponse<IAdherencia>;
export type EntityArrayResponseType = HttpResponse<IAdherencia[]>;

@Injectable({ providedIn: 'root' })
export class AdherenciaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/adherencias');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(adherencia: IAdherencia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(adherencia);
    return this.http
      .post<IAdherencia>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(adherencia: IAdherencia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(adherencia);
    return this.http
      .put<IAdherencia>(`${this.resourceUrl}/${getAdherenciaIdentifier(adherencia) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(adherencia: IAdherencia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(adherencia);
    return this.http
      .patch<IAdherencia>(`${this.resourceUrl}/${getAdherenciaIdentifier(adherencia) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAdherencia>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAdherencia[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAdherenciaToCollectionIfMissing(
    adherenciaCollection: IAdherencia[],
    ...adherenciasToCheck: (IAdherencia | null | undefined)[]
  ): IAdherencia[] {
    const adherencias: IAdherencia[] = adherenciasToCheck.filter(isPresent);
    if (adherencias.length > 0) {
      const adherenciaCollectionIdentifiers = adherenciaCollection.map(adherenciaItem => getAdherenciaIdentifier(adherenciaItem)!);
      const adherenciasToAdd = adherencias.filter(adherenciaItem => {
        const adherenciaIdentifier = getAdherenciaIdentifier(adherenciaItem);
        if (adherenciaIdentifier == null || adherenciaCollectionIdentifiers.includes(adherenciaIdentifier)) {
          return false;
        }
        adherenciaCollectionIdentifiers.push(adherenciaIdentifier);
        return true;
      });
      return [...adherenciasToAdd, ...adherenciaCollection];
    }
    return adherenciaCollection;
  }

  protected convertDateFromClient(adherencia: IAdherencia): IAdherencia {
    return Object.assign({}, adherencia, {
      horaToma: adherencia.horaToma?.isValid() ? adherencia.horaToma.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.horaToma = res.body.horaToma ? dayjs(res.body.horaToma) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((adherencia: IAdherencia) => {
        adherencia.horaToma = adherencia.horaToma ? dayjs(adherencia.horaToma) : undefined;
      });
    }
    return res;
  }
}
