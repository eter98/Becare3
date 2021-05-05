import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPregunta, getPreguntaIdentifier } from '../pregunta.model';

export type EntityResponseType = HttpResponse<IPregunta>;
export type EntityArrayResponseType = HttpResponse<IPregunta[]>;

@Injectable({ providedIn: 'root' })
export class PreguntaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/preguntas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(pregunta: IPregunta): Observable<EntityResponseType> {
    return this.http.post<IPregunta>(this.resourceUrl, pregunta, { observe: 'response' });
  }

  update(pregunta: IPregunta): Observable<EntityResponseType> {
    return this.http.put<IPregunta>(`${this.resourceUrl}/${getPreguntaIdentifier(pregunta) as number}`, pregunta, { observe: 'response' });
  }

  partialUpdate(pregunta: IPregunta): Observable<EntityResponseType> {
    return this.http.patch<IPregunta>(`${this.resourceUrl}/${getPreguntaIdentifier(pregunta) as number}`, pregunta, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPregunta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPregunta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPreguntaToCollectionIfMissing(preguntaCollection: IPregunta[], ...preguntasToCheck: (IPregunta | null | undefined)[]): IPregunta[] {
    const preguntas: IPregunta[] = preguntasToCheck.filter(isPresent);
    if (preguntas.length > 0) {
      const preguntaCollectionIdentifiers = preguntaCollection.map(preguntaItem => getPreguntaIdentifier(preguntaItem)!);
      const preguntasToAdd = preguntas.filter(preguntaItem => {
        const preguntaIdentifier = getPreguntaIdentifier(preguntaItem);
        if (preguntaIdentifier == null || preguntaCollectionIdentifiers.includes(preguntaIdentifier)) {
          return false;
        }
        preguntaCollectionIdentifiers.push(preguntaIdentifier);
        return true;
      });
      return [...preguntasToAdd, ...preguntaCollection];
    }
    return preguntaCollection;
  }
}
