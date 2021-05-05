import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITratamieto, getTratamietoIdentifier } from '../tratamieto.model';

export type EntityResponseType = HttpResponse<ITratamieto>;
export type EntityArrayResponseType = HttpResponse<ITratamieto[]>;

@Injectable({ providedIn: 'root' })
export class TratamietoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/tratamietos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(tratamieto: ITratamieto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tratamieto);
    return this.http
      .post<ITratamieto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tratamieto: ITratamieto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tratamieto);
    return this.http
      .put<ITratamieto>(`${this.resourceUrl}/${getTratamietoIdentifier(tratamieto) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tratamieto: ITratamieto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tratamieto);
    return this.http
      .patch<ITratamieto>(`${this.resourceUrl}/${getTratamietoIdentifier(tratamieto) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITratamieto>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITratamieto[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTratamietoToCollectionIfMissing(
    tratamietoCollection: ITratamieto[],
    ...tratamietosToCheck: (ITratamieto | null | undefined)[]
  ): ITratamieto[] {
    const tratamietos: ITratamieto[] = tratamietosToCheck.filter(isPresent);
    if (tratamietos.length > 0) {
      const tratamietoCollectionIdentifiers = tratamietoCollection.map(tratamietoItem => getTratamietoIdentifier(tratamietoItem)!);
      const tratamietosToAdd = tratamietos.filter(tratamietoItem => {
        const tratamietoIdentifier = getTratamietoIdentifier(tratamietoItem);
        if (tratamietoIdentifier == null || tratamietoCollectionIdentifiers.includes(tratamietoIdentifier)) {
          return false;
        }
        tratamietoCollectionIdentifiers.push(tratamietoIdentifier);
        return true;
      });
      return [...tratamietosToAdd, ...tratamietoCollection];
    }
    return tratamietoCollection;
  }

  protected convertDateFromClient(tratamieto: ITratamieto): ITratamieto {
    return Object.assign({}, tratamieto, {
      fechaInicio: tratamieto.fechaInicio?.isValid() ? tratamieto.fechaInicio.toJSON() : undefined,
      fechaFin: tratamieto.fechaFin?.isValid() ? tratamieto.fechaFin.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaInicio = res.body.fechaInicio ? dayjs(res.body.fechaInicio) : undefined;
      res.body.fechaFin = res.body.fechaFin ? dayjs(res.body.fechaFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tratamieto: ITratamieto) => {
        tratamieto.fechaInicio = tratamieto.fechaInicio ? dayjs(tratamieto.fechaInicio) : undefined;
        tratamieto.fechaFin = tratamieto.fechaFin ? dayjs(tratamieto.fechaFin) : undefined;
      });
    }
    return res;
  }
}
