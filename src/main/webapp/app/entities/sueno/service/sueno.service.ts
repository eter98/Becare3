import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISueno, getSuenoIdentifier } from '../sueno.model';

export type EntityResponseType = HttpResponse<ISueno>;
export type EntityArrayResponseType = HttpResponse<ISueno[]>;

@Injectable({ providedIn: 'root' })
export class SuenoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/suenos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(sueno: ISueno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sueno);
    return this.http
      .post<ISueno>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sueno: ISueno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sueno);
    return this.http
      .put<ISueno>(`${this.resourceUrl}/${getSuenoIdentifier(sueno) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(sueno: ISueno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sueno);
    return this.http
      .patch<ISueno>(`${this.resourceUrl}/${getSuenoIdentifier(sueno) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISueno>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISueno[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSuenoToCollectionIfMissing(suenoCollection: ISueno[], ...suenosToCheck: (ISueno | null | undefined)[]): ISueno[] {
    const suenos: ISueno[] = suenosToCheck.filter(isPresent);
    if (suenos.length > 0) {
      const suenoCollectionIdentifiers = suenoCollection.map(suenoItem => getSuenoIdentifier(suenoItem)!);
      const suenosToAdd = suenos.filter(suenoItem => {
        const suenoIdentifier = getSuenoIdentifier(suenoItem);
        if (suenoIdentifier == null || suenoCollectionIdentifiers.includes(suenoIdentifier)) {
          return false;
        }
        suenoCollectionIdentifiers.push(suenoIdentifier);
        return true;
      });
      return [...suenosToAdd, ...suenoCollection];
    }
    return suenoCollection;
  }

  protected convertDateFromClient(sueno: ISueno): ISueno {
    return Object.assign({}, sueno, {
      timeInstant: sueno.timeInstant?.isValid() ? sueno.timeInstant.toJSON() : undefined,
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
      res.body.forEach((sueno: ISueno) => {
        sueno.timeInstant = sueno.timeInstant ? dayjs(sueno.timeInstant) : undefined;
      });
    }
    return res;
  }
}
