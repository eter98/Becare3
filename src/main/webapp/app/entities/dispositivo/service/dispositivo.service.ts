import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDispositivo, getDispositivoIdentifier } from '../dispositivo.model';

export type EntityResponseType = HttpResponse<IDispositivo>;
export type EntityArrayResponseType = HttpResponse<IDispositivo[]>;

@Injectable({ providedIn: 'root' })
export class DispositivoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/dispositivos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(dispositivo: IDispositivo): Observable<EntityResponseType> {
    return this.http.post<IDispositivo>(this.resourceUrl, dispositivo, { observe: 'response' });
  }

  update(dispositivo: IDispositivo): Observable<EntityResponseType> {
    return this.http.put<IDispositivo>(`${this.resourceUrl}/${getDispositivoIdentifier(dispositivo) as number}`, dispositivo, {
      observe: 'response',
    });
  }

  partialUpdate(dispositivo: IDispositivo): Observable<EntityResponseType> {
    return this.http.patch<IDispositivo>(`${this.resourceUrl}/${getDispositivoIdentifier(dispositivo) as number}`, dispositivo, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDispositivo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDispositivo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDispositivoToCollectionIfMissing(
    dispositivoCollection: IDispositivo[],
    ...dispositivosToCheck: (IDispositivo | null | undefined)[]
  ): IDispositivo[] {
    const dispositivos: IDispositivo[] = dispositivosToCheck.filter(isPresent);
    if (dispositivos.length > 0) {
      const dispositivoCollectionIdentifiers = dispositivoCollection.map(dispositivoItem => getDispositivoIdentifier(dispositivoItem)!);
      const dispositivosToAdd = dispositivos.filter(dispositivoItem => {
        const dispositivoIdentifier = getDispositivoIdentifier(dispositivoItem);
        if (dispositivoIdentifier == null || dispositivoCollectionIdentifiers.includes(dispositivoIdentifier)) {
          return false;
        }
        dispositivoCollectionIdentifiers.push(dispositivoIdentifier);
        return true;
      });
      return [...dispositivosToAdd, ...dispositivoCollection];
    }
    return dispositivoCollection;
  }
}
