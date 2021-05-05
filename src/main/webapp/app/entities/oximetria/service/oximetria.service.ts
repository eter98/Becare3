import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOximetria, getOximetriaIdentifier } from '../oximetria.model';

export type EntityResponseType = HttpResponse<IOximetria>;
export type EntityArrayResponseType = HttpResponse<IOximetria[]>;

@Injectable({ providedIn: 'root' })
export class OximetriaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/oximetrias');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(oximetria: IOximetria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(oximetria);
    return this.http
      .post<IOximetria>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(oximetria: IOximetria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(oximetria);
    return this.http
      .put<IOximetria>(`${this.resourceUrl}/${getOximetriaIdentifier(oximetria) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(oximetria: IOximetria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(oximetria);
    return this.http
      .patch<IOximetria>(`${this.resourceUrl}/${getOximetriaIdentifier(oximetria) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOximetria>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOximetria[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOximetriaToCollectionIfMissing(
    oximetriaCollection: IOximetria[],
    ...oximetriasToCheck: (IOximetria | null | undefined)[]
  ): IOximetria[] {
    const oximetrias: IOximetria[] = oximetriasToCheck.filter(isPresent);
    if (oximetrias.length > 0) {
      const oximetriaCollectionIdentifiers = oximetriaCollection.map(oximetriaItem => getOximetriaIdentifier(oximetriaItem)!);
      const oximetriasToAdd = oximetrias.filter(oximetriaItem => {
        const oximetriaIdentifier = getOximetriaIdentifier(oximetriaItem);
        if (oximetriaIdentifier == null || oximetriaCollectionIdentifiers.includes(oximetriaIdentifier)) {
          return false;
        }
        oximetriaCollectionIdentifiers.push(oximetriaIdentifier);
        return true;
      });
      return [...oximetriasToAdd, ...oximetriaCollection];
    }
    return oximetriaCollection;
  }

  protected convertDateFromClient(oximetria: IOximetria): IOximetria {
    return Object.assign({}, oximetria, {
      fechaRegistro: oximetria.fechaRegistro?.isValid() ? oximetria.fechaRegistro.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaRegistro = res.body.fechaRegistro ? dayjs(res.body.fechaRegistro) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((oximetria: IOximetria) => {
        oximetria.fechaRegistro = oximetria.fechaRegistro ? dayjs(oximetria.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
