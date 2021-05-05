import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICondicion, getCondicionIdentifier } from '../condicion.model';

export type EntityResponseType = HttpResponse<ICondicion>;
export type EntityArrayResponseType = HttpResponse<ICondicion[]>;

@Injectable({ providedIn: 'root' })
export class CondicionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/condicions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(condicion: ICondicion): Observable<EntityResponseType> {
    return this.http.post<ICondicion>(this.resourceUrl, condicion, { observe: 'response' });
  }

  update(condicion: ICondicion): Observable<EntityResponseType> {
    return this.http.put<ICondicion>(`${this.resourceUrl}/${getCondicionIdentifier(condicion) as number}`, condicion, {
      observe: 'response',
    });
  }

  partialUpdate(condicion: ICondicion): Observable<EntityResponseType> {
    return this.http.patch<ICondicion>(`${this.resourceUrl}/${getCondicionIdentifier(condicion) as number}`, condicion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICondicion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICondicion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCondicionToCollectionIfMissing(
    condicionCollection: ICondicion[],
    ...condicionsToCheck: (ICondicion | null | undefined)[]
  ): ICondicion[] {
    const condicions: ICondicion[] = condicionsToCheck.filter(isPresent);
    if (condicions.length > 0) {
      const condicionCollectionIdentifiers = condicionCollection.map(condicionItem => getCondicionIdentifier(condicionItem)!);
      const condicionsToAdd = condicions.filter(condicionItem => {
        const condicionIdentifier = getCondicionIdentifier(condicionItem);
        if (condicionIdentifier == null || condicionCollectionIdentifiers.includes(condicionIdentifier)) {
          return false;
        }
        condicionCollectionIdentifiers.push(condicionIdentifier);
        return true;
      });
      return [...condicionsToAdd, ...condicionCollection];
    }
    return condicionCollection;
  }
}
