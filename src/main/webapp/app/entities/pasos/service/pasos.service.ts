import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPasos, getPasosIdentifier } from '../pasos.model';

export type EntityResponseType = HttpResponse<IPasos>;
export type EntityArrayResponseType = HttpResponse<IPasos[]>;

@Injectable({ providedIn: 'root' })
export class PasosService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/pasos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(pasos: IPasos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pasos);
    return this.http
      .post<IPasos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(pasos: IPasos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pasos);
    return this.http
      .put<IPasos>(`${this.resourceUrl}/${getPasosIdentifier(pasos) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(pasos: IPasos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pasos);
    return this.http
      .patch<IPasos>(`${this.resourceUrl}/${getPasosIdentifier(pasos) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPasos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPasos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPasosToCollectionIfMissing(pasosCollection: IPasos[], ...pasosToCheck: (IPasos | null | undefined)[]): IPasos[] {
    const pasos: IPasos[] = pasosToCheck.filter(isPresent);
    if (pasos.length > 0) {
      const pasosCollectionIdentifiers = pasosCollection.map(pasosItem => getPasosIdentifier(pasosItem)!);
      const pasosToAdd = pasos.filter(pasosItem => {
        const pasosIdentifier = getPasosIdentifier(pasosItem);
        if (pasosIdentifier == null || pasosCollectionIdentifiers.includes(pasosIdentifier)) {
          return false;
        }
        pasosCollectionIdentifiers.push(pasosIdentifier);
        return true;
      });
      return [...pasosToAdd, ...pasosCollection];
    }
    return pasosCollection;
  }

  protected convertDateFromClient(pasos: IPasos): IPasos {
    return Object.assign({}, pasos, {
      timeInstant: pasos.timeInstant?.isValid() ? pasos.timeInstant.toJSON() : undefined,
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
      res.body.forEach((pasos: IPasos) => {
        pasos.timeInstant = pasos.timeInstant ? dayjs(pasos.timeInstant) : undefined;
      });
    }
    return res;
  }
}
