import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFarmaceutica, getFarmaceuticaIdentifier } from '../farmaceutica.model';

export type EntityResponseType = HttpResponse<IFarmaceutica>;
export type EntityArrayResponseType = HttpResponse<IFarmaceutica[]>;

@Injectable({ providedIn: 'root' })
export class FarmaceuticaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/farmaceuticas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(farmaceutica: IFarmaceutica): Observable<EntityResponseType> {
    return this.http.post<IFarmaceutica>(this.resourceUrl, farmaceutica, { observe: 'response' });
  }

  update(farmaceutica: IFarmaceutica): Observable<EntityResponseType> {
    return this.http.put<IFarmaceutica>(`${this.resourceUrl}/${getFarmaceuticaIdentifier(farmaceutica) as number}`, farmaceutica, {
      observe: 'response',
    });
  }

  partialUpdate(farmaceutica: IFarmaceutica): Observable<EntityResponseType> {
    return this.http.patch<IFarmaceutica>(`${this.resourceUrl}/${getFarmaceuticaIdentifier(farmaceutica) as number}`, farmaceutica, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFarmaceutica>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFarmaceutica[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFarmaceuticaToCollectionIfMissing(
    farmaceuticaCollection: IFarmaceutica[],
    ...farmaceuticasToCheck: (IFarmaceutica | null | undefined)[]
  ): IFarmaceutica[] {
    const farmaceuticas: IFarmaceutica[] = farmaceuticasToCheck.filter(isPresent);
    if (farmaceuticas.length > 0) {
      const farmaceuticaCollectionIdentifiers = farmaceuticaCollection.map(
        farmaceuticaItem => getFarmaceuticaIdentifier(farmaceuticaItem)!
      );
      const farmaceuticasToAdd = farmaceuticas.filter(farmaceuticaItem => {
        const farmaceuticaIdentifier = getFarmaceuticaIdentifier(farmaceuticaItem);
        if (farmaceuticaIdentifier == null || farmaceuticaCollectionIdentifiers.includes(farmaceuticaIdentifier)) {
          return false;
        }
        farmaceuticaCollectionIdentifiers.push(farmaceuticaIdentifier);
        return true;
      });
      return [...farmaceuticasToAdd, ...farmaceuticaCollection];
    }
    return farmaceuticaCollection;
  }
}
