import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPeso, getPesoIdentifier } from '../peso.model';

export type EntityResponseType = HttpResponse<IPeso>;
export type EntityArrayResponseType = HttpResponse<IPeso[]>;

@Injectable({ providedIn: 'root' })
export class PesoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/pesos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(peso: IPeso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(peso);
    return this.http
      .post<IPeso>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(peso: IPeso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(peso);
    return this.http
      .put<IPeso>(`${this.resourceUrl}/${getPesoIdentifier(peso) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(peso: IPeso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(peso);
    return this.http
      .patch<IPeso>(`${this.resourceUrl}/${getPesoIdentifier(peso) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPeso>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPeso[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPesoToCollectionIfMissing(pesoCollection: IPeso[], ...pesosToCheck: (IPeso | null | undefined)[]): IPeso[] {
    const pesos: IPeso[] = pesosToCheck.filter(isPresent);
    if (pesos.length > 0) {
      const pesoCollectionIdentifiers = pesoCollection.map(pesoItem => getPesoIdentifier(pesoItem)!);
      const pesosToAdd = pesos.filter(pesoItem => {
        const pesoIdentifier = getPesoIdentifier(pesoItem);
        if (pesoIdentifier == null || pesoCollectionIdentifiers.includes(pesoIdentifier)) {
          return false;
        }
        pesoCollectionIdentifiers.push(pesoIdentifier);
        return true;
      });
      return [...pesosToAdd, ...pesoCollection];
    }
    return pesoCollection;
  }

  protected convertDateFromClient(peso: IPeso): IPeso {
    return Object.assign({}, peso, {
      fechaRegistro: peso.fechaRegistro?.isValid() ? peso.fechaRegistro.toJSON() : undefined,
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
      res.body.forEach((peso: IPeso) => {
        peso.fechaRegistro = peso.fechaRegistro ? dayjs(peso.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
