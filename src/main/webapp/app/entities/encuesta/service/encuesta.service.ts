import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEncuesta, getEncuestaIdentifier } from '../encuesta.model';

export type EntityResponseType = HttpResponse<IEncuesta>;
export type EntityArrayResponseType = HttpResponse<IEncuesta[]>;

@Injectable({ providedIn: 'root' })
export class EncuestaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/encuestas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(encuesta: IEncuesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(encuesta);
    return this.http
      .post<IEncuesta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(encuesta: IEncuesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(encuesta);
    return this.http
      .put<IEncuesta>(`${this.resourceUrl}/${getEncuestaIdentifier(encuesta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(encuesta: IEncuesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(encuesta);
    return this.http
      .patch<IEncuesta>(`${this.resourceUrl}/${getEncuestaIdentifier(encuesta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEncuesta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEncuesta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEncuestaToCollectionIfMissing(encuestaCollection: IEncuesta[], ...encuestasToCheck: (IEncuesta | null | undefined)[]): IEncuesta[] {
    const encuestas: IEncuesta[] = encuestasToCheck.filter(isPresent);
    if (encuestas.length > 0) {
      const encuestaCollectionIdentifiers = encuestaCollection.map(encuestaItem => getEncuestaIdentifier(encuestaItem)!);
      const encuestasToAdd = encuestas.filter(encuestaItem => {
        const encuestaIdentifier = getEncuestaIdentifier(encuestaItem);
        if (encuestaIdentifier == null || encuestaCollectionIdentifiers.includes(encuestaIdentifier)) {
          return false;
        }
        encuestaCollectionIdentifiers.push(encuestaIdentifier);
        return true;
      });
      return [...encuestasToAdd, ...encuestaCollection];
    }
    return encuestaCollection;
  }

  protected convertDateFromClient(encuesta: IEncuesta): IEncuesta {
    return Object.assign({}, encuesta, {
      fecha: encuesta.fecha?.isValid() ? encuesta.fecha.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fecha = res.body.fecha ? dayjs(res.body.fecha) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((encuesta: IEncuesta) => {
        encuesta.fecha = encuesta.fecha ? dayjs(encuesta.fecha) : undefined;
      });
    }
    return res;
  }
}
