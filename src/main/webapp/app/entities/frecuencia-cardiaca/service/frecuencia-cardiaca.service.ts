import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFrecuenciaCardiaca, getFrecuenciaCardiacaIdentifier } from '../frecuencia-cardiaca.model';

export type EntityResponseType = HttpResponse<IFrecuenciaCardiaca>;
export type EntityArrayResponseType = HttpResponse<IFrecuenciaCardiaca[]>;

@Injectable({ providedIn: 'root' })
export class FrecuenciaCardiacaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/frecuencia-cardiacas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(frecuenciaCardiaca: IFrecuenciaCardiaca): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frecuenciaCardiaca);
    return this.http
      .post<IFrecuenciaCardiaca>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(frecuenciaCardiaca: IFrecuenciaCardiaca): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frecuenciaCardiaca);
    return this.http
      .put<IFrecuenciaCardiaca>(`${this.resourceUrl}/${getFrecuenciaCardiacaIdentifier(frecuenciaCardiaca) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(frecuenciaCardiaca: IFrecuenciaCardiaca): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frecuenciaCardiaca);
    return this.http
      .patch<IFrecuenciaCardiaca>(`${this.resourceUrl}/${getFrecuenciaCardiacaIdentifier(frecuenciaCardiaca) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFrecuenciaCardiaca>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFrecuenciaCardiaca[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFrecuenciaCardiacaToCollectionIfMissing(
    frecuenciaCardiacaCollection: IFrecuenciaCardiaca[],
    ...frecuenciaCardiacasToCheck: (IFrecuenciaCardiaca | null | undefined)[]
  ): IFrecuenciaCardiaca[] {
    const frecuenciaCardiacas: IFrecuenciaCardiaca[] = frecuenciaCardiacasToCheck.filter(isPresent);
    if (frecuenciaCardiacas.length > 0) {
      const frecuenciaCardiacaCollectionIdentifiers = frecuenciaCardiacaCollection.map(
        frecuenciaCardiacaItem => getFrecuenciaCardiacaIdentifier(frecuenciaCardiacaItem)!
      );
      const frecuenciaCardiacasToAdd = frecuenciaCardiacas.filter(frecuenciaCardiacaItem => {
        const frecuenciaCardiacaIdentifier = getFrecuenciaCardiacaIdentifier(frecuenciaCardiacaItem);
        if (frecuenciaCardiacaIdentifier == null || frecuenciaCardiacaCollectionIdentifiers.includes(frecuenciaCardiacaIdentifier)) {
          return false;
        }
        frecuenciaCardiacaCollectionIdentifiers.push(frecuenciaCardiacaIdentifier);
        return true;
      });
      return [...frecuenciaCardiacasToAdd, ...frecuenciaCardiacaCollection];
    }
    return frecuenciaCardiacaCollection;
  }

  protected convertDateFromClient(frecuenciaCardiaca: IFrecuenciaCardiaca): IFrecuenciaCardiaca {
    return Object.assign({}, frecuenciaCardiaca, {
      fechaRegistro: frecuenciaCardiaca.fechaRegistro?.isValid() ? frecuenciaCardiaca.fechaRegistro.toJSON() : undefined,
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
      res.body.forEach((frecuenciaCardiaca: IFrecuenciaCardiaca) => {
        frecuenciaCardiaca.fechaRegistro = frecuenciaCardiaca.fechaRegistro ? dayjs(frecuenciaCardiaca.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
