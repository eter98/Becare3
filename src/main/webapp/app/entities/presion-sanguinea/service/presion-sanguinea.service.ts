import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPresionSanguinea, getPresionSanguineaIdentifier } from '../presion-sanguinea.model';

export type EntityResponseType = HttpResponse<IPresionSanguinea>;
export type EntityArrayResponseType = HttpResponse<IPresionSanguinea[]>;

@Injectable({ providedIn: 'root' })
export class PresionSanguineaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/presion-sanguineas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(presionSanguinea: IPresionSanguinea): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(presionSanguinea);
    return this.http
      .post<IPresionSanguinea>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(presionSanguinea: IPresionSanguinea): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(presionSanguinea);
    return this.http
      .put<IPresionSanguinea>(`${this.resourceUrl}/${getPresionSanguineaIdentifier(presionSanguinea) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(presionSanguinea: IPresionSanguinea): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(presionSanguinea);
    return this.http
      .patch<IPresionSanguinea>(`${this.resourceUrl}/${getPresionSanguineaIdentifier(presionSanguinea) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPresionSanguinea>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPresionSanguinea[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPresionSanguineaToCollectionIfMissing(
    presionSanguineaCollection: IPresionSanguinea[],
    ...presionSanguineasToCheck: (IPresionSanguinea | null | undefined)[]
  ): IPresionSanguinea[] {
    const presionSanguineas: IPresionSanguinea[] = presionSanguineasToCheck.filter(isPresent);
    if (presionSanguineas.length > 0) {
      const presionSanguineaCollectionIdentifiers = presionSanguineaCollection.map(
        presionSanguineaItem => getPresionSanguineaIdentifier(presionSanguineaItem)!
      );
      const presionSanguineasToAdd = presionSanguineas.filter(presionSanguineaItem => {
        const presionSanguineaIdentifier = getPresionSanguineaIdentifier(presionSanguineaItem);
        if (presionSanguineaIdentifier == null || presionSanguineaCollectionIdentifiers.includes(presionSanguineaIdentifier)) {
          return false;
        }
        presionSanguineaCollectionIdentifiers.push(presionSanguineaIdentifier);
        return true;
      });
      return [...presionSanguineasToAdd, ...presionSanguineaCollection];
    }
    return presionSanguineaCollection;
  }

  protected convertDateFromClient(presionSanguinea: IPresionSanguinea): IPresionSanguinea {
    return Object.assign({}, presionSanguinea, {
      fechaRegistro: presionSanguinea.fechaRegistro?.isValid() ? presionSanguinea.fechaRegistro.toJSON() : undefined,
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
      res.body.forEach((presionSanguinea: IPresionSanguinea) => {
        presionSanguinea.fechaRegistro = presionSanguinea.fechaRegistro ? dayjs(presionSanguinea.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
