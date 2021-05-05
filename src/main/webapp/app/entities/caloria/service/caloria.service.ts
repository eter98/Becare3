import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICaloria, getCaloriaIdentifier } from '../caloria.model';

export type EntityResponseType = HttpResponse<ICaloria>;
export type EntityArrayResponseType = HttpResponse<ICaloria[]>;

@Injectable({ providedIn: 'root' })
export class CaloriaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/calorias');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(caloria: ICaloria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(caloria);
    return this.http
      .post<ICaloria>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(caloria: ICaloria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(caloria);
    return this.http
      .put<ICaloria>(`${this.resourceUrl}/${getCaloriaIdentifier(caloria) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(caloria: ICaloria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(caloria);
    return this.http
      .patch<ICaloria>(`${this.resourceUrl}/${getCaloriaIdentifier(caloria) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICaloria>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICaloria[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCaloriaToCollectionIfMissing(caloriaCollection: ICaloria[], ...caloriasToCheck: (ICaloria | null | undefined)[]): ICaloria[] {
    const calorias: ICaloria[] = caloriasToCheck.filter(isPresent);
    if (calorias.length > 0) {
      const caloriaCollectionIdentifiers = caloriaCollection.map(caloriaItem => getCaloriaIdentifier(caloriaItem)!);
      const caloriasToAdd = calorias.filter(caloriaItem => {
        const caloriaIdentifier = getCaloriaIdentifier(caloriaItem);
        if (caloriaIdentifier == null || caloriaCollectionIdentifiers.includes(caloriaIdentifier)) {
          return false;
        }
        caloriaCollectionIdentifiers.push(caloriaIdentifier);
        return true;
      });
      return [...caloriasToAdd, ...caloriaCollection];
    }
    return caloriaCollection;
  }

  protected convertDateFromClient(caloria: ICaloria): ICaloria {
    return Object.assign({}, caloria, {
      fechaRegistro: caloria.fechaRegistro?.isValid() ? caloria.fechaRegistro.toJSON() : undefined,
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
      res.body.forEach((caloria: ICaloria) => {
        caloria.fechaRegistro = caloria.fechaRegistro ? dayjs(caloria.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
