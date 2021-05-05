import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAgenda, getAgendaIdentifier } from '../agenda.model';

export type EntityResponseType = HttpResponse<IAgenda>;
export type EntityArrayResponseType = HttpResponse<IAgenda[]>;

@Injectable({ providedIn: 'root' })
export class AgendaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/agenda');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(agenda: IAgenda): Observable<EntityResponseType> {
    return this.http.post<IAgenda>(this.resourceUrl, agenda, { observe: 'response' });
  }

  update(agenda: IAgenda): Observable<EntityResponseType> {
    return this.http.put<IAgenda>(`${this.resourceUrl}/${getAgendaIdentifier(agenda) as number}`, agenda, { observe: 'response' });
  }

  partialUpdate(agenda: IAgenda): Observable<EntityResponseType> {
    return this.http.patch<IAgenda>(`${this.resourceUrl}/${getAgendaIdentifier(agenda) as number}`, agenda, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAgenda>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAgenda[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAgendaToCollectionIfMissing(agendaCollection: IAgenda[], ...agendaToCheck: (IAgenda | null | undefined)[]): IAgenda[] {
    const agenda: IAgenda[] = agendaToCheck.filter(isPresent);
    if (agenda.length > 0) {
      const agendaCollectionIdentifiers = agendaCollection.map(agendaItem => getAgendaIdentifier(agendaItem)!);
      const agendaToAdd = agenda.filter(agendaItem => {
        const agendaIdentifier = getAgendaIdentifier(agendaItem);
        if (agendaIdentifier == null || agendaCollectionIdentifiers.includes(agendaIdentifier)) {
          return false;
        }
        agendaCollectionIdentifiers.push(agendaIdentifier);
        return true;
      });
      return [...agendaToAdd, ...agendaCollection];
    }
    return agendaCollection;
  }
}
