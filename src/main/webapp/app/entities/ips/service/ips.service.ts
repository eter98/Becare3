import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIPS, getIPSIdentifier } from '../ips.model';

export type EntityResponseType = HttpResponse<IIPS>;
export type EntityArrayResponseType = HttpResponse<IIPS[]>;

@Injectable({ providedIn: 'root' })
export class IPSService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/ips');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(iPS: IIPS): Observable<EntityResponseType> {
    return this.http.post<IIPS>(this.resourceUrl, iPS, { observe: 'response' });
  }

  update(iPS: IIPS): Observable<EntityResponseType> {
    return this.http.put<IIPS>(`${this.resourceUrl}/${getIPSIdentifier(iPS) as number}`, iPS, { observe: 'response' });
  }

  partialUpdate(iPS: IIPS): Observable<EntityResponseType> {
    return this.http.patch<IIPS>(`${this.resourceUrl}/${getIPSIdentifier(iPS) as number}`, iPS, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIPS>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIPS[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIPSToCollectionIfMissing(iPSCollection: IIPS[], ...iPSToCheck: (IIPS | null | undefined)[]): IIPS[] {
    const iPS: IIPS[] = iPSToCheck.filter(isPresent);
    if (iPS.length > 0) {
      const iPSCollectionIdentifiers = iPSCollection.map(iPSItem => getIPSIdentifier(iPSItem)!);
      const iPSToAdd = iPS.filter(iPSItem => {
        const iPSIdentifier = getIPSIdentifier(iPSItem);
        if (iPSIdentifier == null || iPSCollectionIdentifiers.includes(iPSIdentifier)) {
          return false;
        }
        iPSCollectionIdentifiers.push(iPSIdentifier);
        return true;
      });
      return [...iPSToAdd, ...iPSCollection];
    }
    return iPSCollection;
  }
}
