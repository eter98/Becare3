import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaciente, getPacienteIdentifier } from '../paciente.model';

export type EntityResponseType = HttpResponse<IPaciente>;
export type EntityArrayResponseType = HttpResponse<IPaciente[]>;

@Injectable({ providedIn: 'root' })
export class PacienteService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/pacientes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(paciente: IPaciente): Observable<EntityResponseType> {
    return this.http.post<IPaciente>(this.resourceUrl, paciente, { observe: 'response' });
  }

  update(paciente: IPaciente): Observable<EntityResponseType> {
    return this.http.put<IPaciente>(`${this.resourceUrl}/${getPacienteIdentifier(paciente) as number}`, paciente, { observe: 'response' });
  }

  partialUpdate(paciente: IPaciente): Observable<EntityResponseType> {
    return this.http.patch<IPaciente>(`${this.resourceUrl}/${getPacienteIdentifier(paciente) as number}`, paciente, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaciente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaciente[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPacienteToCollectionIfMissing(pacienteCollection: IPaciente[], ...pacientesToCheck: (IPaciente | null | undefined)[]): IPaciente[] {
    const pacientes: IPaciente[] = pacientesToCheck.filter(isPresent);
    if (pacientes.length > 0) {
      const pacienteCollectionIdentifiers = pacienteCollection.map(pacienteItem => getPacienteIdentifier(pacienteItem)!);
      const pacientesToAdd = pacientes.filter(pacienteItem => {
        const pacienteIdentifier = getPacienteIdentifier(pacienteItem);
        if (pacienteIdentifier == null || pacienteCollectionIdentifiers.includes(pacienteIdentifier)) {
          return false;
        }
        pacienteCollectionIdentifiers.push(pacienteIdentifier);
        return true;
      });
      return [...pacientesToAdd, ...pacienteCollection];
    }
    return pacienteCollection;
  }
}
