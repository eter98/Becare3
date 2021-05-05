import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITratamientoMedicamento, getTratamientoMedicamentoIdentifier } from '../tratamiento-medicamento.model';

export type EntityResponseType = HttpResponse<ITratamientoMedicamento>;
export type EntityArrayResponseType = HttpResponse<ITratamientoMedicamento[]>;

@Injectable({ providedIn: 'root' })
export class TratamientoMedicamentoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/tratamiento-medicamentos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(tratamientoMedicamento: ITratamientoMedicamento): Observable<EntityResponseType> {
    return this.http.post<ITratamientoMedicamento>(this.resourceUrl, tratamientoMedicamento, { observe: 'response' });
  }

  update(tratamientoMedicamento: ITratamientoMedicamento): Observable<EntityResponseType> {
    return this.http.put<ITratamientoMedicamento>(
      `${this.resourceUrl}/${getTratamientoMedicamentoIdentifier(tratamientoMedicamento) as number}`,
      tratamientoMedicamento,
      { observe: 'response' }
    );
  }

  partialUpdate(tratamientoMedicamento: ITratamientoMedicamento): Observable<EntityResponseType> {
    return this.http.patch<ITratamientoMedicamento>(
      `${this.resourceUrl}/${getTratamientoMedicamentoIdentifier(tratamientoMedicamento) as number}`,
      tratamientoMedicamento,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITratamientoMedicamento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITratamientoMedicamento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTratamientoMedicamentoToCollectionIfMissing(
    tratamientoMedicamentoCollection: ITratamientoMedicamento[],
    ...tratamientoMedicamentosToCheck: (ITratamientoMedicamento | null | undefined)[]
  ): ITratamientoMedicamento[] {
    const tratamientoMedicamentos: ITratamientoMedicamento[] = tratamientoMedicamentosToCheck.filter(isPresent);
    if (tratamientoMedicamentos.length > 0) {
      const tratamientoMedicamentoCollectionIdentifiers = tratamientoMedicamentoCollection.map(
        tratamientoMedicamentoItem => getTratamientoMedicamentoIdentifier(tratamientoMedicamentoItem)!
      );
      const tratamientoMedicamentosToAdd = tratamientoMedicamentos.filter(tratamientoMedicamentoItem => {
        const tratamientoMedicamentoIdentifier = getTratamientoMedicamentoIdentifier(tratamientoMedicamentoItem);
        if (
          tratamientoMedicamentoIdentifier == null ||
          tratamientoMedicamentoCollectionIdentifiers.includes(tratamientoMedicamentoIdentifier)
        ) {
          return false;
        }
        tratamientoMedicamentoCollectionIdentifiers.push(tratamientoMedicamentoIdentifier);
        return true;
      });
      return [...tratamientoMedicamentosToAdd, ...tratamientoMedicamentoCollection];
    }
    return tratamientoMedicamentoCollection;
  }
}
