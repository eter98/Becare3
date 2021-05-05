import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAlarma, getAlarmaIdentifier } from '../alarma.model';

export type EntityResponseType = HttpResponse<IAlarma>;
export type EntityArrayResponseType = HttpResponse<IAlarma[]>;

@Injectable({ providedIn: 'root' })
export class AlarmaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/alarmas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(alarma: IAlarma): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alarma);
    return this.http
      .post<IAlarma>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(alarma: IAlarma): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alarma);
    return this.http
      .put<IAlarma>(`${this.resourceUrl}/${getAlarmaIdentifier(alarma) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(alarma: IAlarma): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alarma);
    return this.http
      .patch<IAlarma>(`${this.resourceUrl}/${getAlarmaIdentifier(alarma) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAlarma>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAlarma[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAlarmaToCollectionIfMissing(alarmaCollection: IAlarma[], ...alarmasToCheck: (IAlarma | null | undefined)[]): IAlarma[] {
    const alarmas: IAlarma[] = alarmasToCheck.filter(isPresent);
    if (alarmas.length > 0) {
      const alarmaCollectionIdentifiers = alarmaCollection.map(alarmaItem => getAlarmaIdentifier(alarmaItem)!);
      const alarmasToAdd = alarmas.filter(alarmaItem => {
        const alarmaIdentifier = getAlarmaIdentifier(alarmaItem);
        if (alarmaIdentifier == null || alarmaCollectionIdentifiers.includes(alarmaIdentifier)) {
          return false;
        }
        alarmaCollectionIdentifiers.push(alarmaIdentifier);
        return true;
      });
      return [...alarmasToAdd, ...alarmaCollection];
    }
    return alarmaCollection;
  }

  protected convertDateFromClient(alarma: IAlarma): IAlarma {
    return Object.assign({}, alarma, {
      timeInstant: alarma.timeInstant?.isValid() ? alarma.timeInstant.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeInstant = res.body.timeInstant ? dayjs(res.body.timeInstant) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((alarma: IAlarma) => {
        alarma.timeInstant = alarma.timeInstant ? dayjs(alarma.timeInstant) : undefined;
      });
    }
    return res;
  }
}
