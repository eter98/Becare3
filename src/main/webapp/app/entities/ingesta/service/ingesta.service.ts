import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIngesta, getIngestaIdentifier } from '../ingesta.model';

export type EntityResponseType = HttpResponse<IIngesta>;
export type EntityArrayResponseType = HttpResponse<IIngesta[]>;

@Injectable({ providedIn: 'root' })
export class IngestaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/ingestas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(ingesta: IIngesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ingesta);
    return this.http
      .post<IIngesta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ingesta: IIngesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ingesta);
    return this.http
      .put<IIngesta>(`${this.resourceUrl}/${getIngestaIdentifier(ingesta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(ingesta: IIngesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ingesta);
    return this.http
      .patch<IIngesta>(`${this.resourceUrl}/${getIngestaIdentifier(ingesta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIngesta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIngesta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIngestaToCollectionIfMissing(ingestaCollection: IIngesta[], ...ingestasToCheck: (IIngesta | null | undefined)[]): IIngesta[] {
    const ingestas: IIngesta[] = ingestasToCheck.filter(isPresent);
    if (ingestas.length > 0) {
      const ingestaCollectionIdentifiers = ingestaCollection.map(ingestaItem => getIngestaIdentifier(ingestaItem)!);
      const ingestasToAdd = ingestas.filter(ingestaItem => {
        const ingestaIdentifier = getIngestaIdentifier(ingestaItem);
        if (ingestaIdentifier == null || ingestaCollectionIdentifiers.includes(ingestaIdentifier)) {
          return false;
        }
        ingestaCollectionIdentifiers.push(ingestaIdentifier);
        return true;
      });
      return [...ingestasToAdd, ...ingestaCollection];
    }
    return ingestaCollection;
  }

  protected convertDateFromClient(ingesta: IIngesta): IIngesta {
    return Object.assign({}, ingesta, {
      fechaRegistro: ingesta.fechaRegistro?.isValid() ? ingesta.fechaRegistro.toJSON() : undefined,
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
      res.body.forEach((ingesta: IIngesta) => {
        ingesta.fechaRegistro = ingesta.fechaRegistro ? dayjs(ingesta.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
