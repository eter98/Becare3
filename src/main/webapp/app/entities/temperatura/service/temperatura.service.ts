import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITemperatura, getTemperaturaIdentifier } from '../temperatura.model';

export type EntityResponseType = HttpResponse<ITemperatura>;
export type EntityArrayResponseType = HttpResponse<ITemperatura[]>;

@Injectable({ providedIn: 'root' })
export class TemperaturaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/temperaturas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(temperatura: ITemperatura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(temperatura);
    return this.http
      .post<ITemperatura>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(temperatura: ITemperatura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(temperatura);
    return this.http
      .put<ITemperatura>(`${this.resourceUrl}/${getTemperaturaIdentifier(temperatura) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(temperatura: ITemperatura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(temperatura);
    return this.http
      .patch<ITemperatura>(`${this.resourceUrl}/${getTemperaturaIdentifier(temperatura) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITemperatura>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITemperatura[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTemperaturaToCollectionIfMissing(
    temperaturaCollection: ITemperatura[],
    ...temperaturasToCheck: (ITemperatura | null | undefined)[]
  ): ITemperatura[] {
    const temperaturas: ITemperatura[] = temperaturasToCheck.filter(isPresent);
    if (temperaturas.length > 0) {
      const temperaturaCollectionIdentifiers = temperaturaCollection.map(temperaturaItem => getTemperaturaIdentifier(temperaturaItem)!);
      const temperaturasToAdd = temperaturas.filter(temperaturaItem => {
        const temperaturaIdentifier = getTemperaturaIdentifier(temperaturaItem);
        if (temperaturaIdentifier == null || temperaturaCollectionIdentifiers.includes(temperaturaIdentifier)) {
          return false;
        }
        temperaturaCollectionIdentifiers.push(temperaturaIdentifier);
        return true;
      });
      return [...temperaturasToAdd, ...temperaturaCollection];
    }
    return temperaturaCollection;
  }

  protected convertDateFromClient(temperatura: ITemperatura): ITemperatura {
    return Object.assign({}, temperatura, {
      fechaRegistro: temperatura.fechaRegistro?.isValid() ? temperatura.fechaRegistro.toJSON() : undefined,
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
      res.body.forEach((temperatura: ITemperatura) => {
        temperatura.fechaRegistro = temperatura.fechaRegistro ? dayjs(temperatura.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
