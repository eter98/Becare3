import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICuestionarioEstado, getCuestionarioEstadoIdentifier } from '../cuestionario-estado.model';

export type EntityResponseType = HttpResponse<ICuestionarioEstado>;
export type EntityArrayResponseType = HttpResponse<ICuestionarioEstado[]>;

@Injectable({ providedIn: 'root' })
export class CuestionarioEstadoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/cuestionario-estados');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(cuestionarioEstado: ICuestionarioEstado): Observable<EntityResponseType> {
    return this.http.post<ICuestionarioEstado>(this.resourceUrl, cuestionarioEstado, { observe: 'response' });
  }

  update(cuestionarioEstado: ICuestionarioEstado): Observable<EntityResponseType> {
    return this.http.put<ICuestionarioEstado>(
      `${this.resourceUrl}/${getCuestionarioEstadoIdentifier(cuestionarioEstado) as number}`,
      cuestionarioEstado,
      { observe: 'response' }
    );
  }

  partialUpdate(cuestionarioEstado: ICuestionarioEstado): Observable<EntityResponseType> {
    return this.http.patch<ICuestionarioEstado>(
      `${this.resourceUrl}/${getCuestionarioEstadoIdentifier(cuestionarioEstado) as number}`,
      cuestionarioEstado,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICuestionarioEstado>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICuestionarioEstado[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCuestionarioEstadoToCollectionIfMissing(
    cuestionarioEstadoCollection: ICuestionarioEstado[],
    ...cuestionarioEstadosToCheck: (ICuestionarioEstado | null | undefined)[]
  ): ICuestionarioEstado[] {
    const cuestionarioEstados: ICuestionarioEstado[] = cuestionarioEstadosToCheck.filter(isPresent);
    if (cuestionarioEstados.length > 0) {
      const cuestionarioEstadoCollectionIdentifiers = cuestionarioEstadoCollection.map(
        cuestionarioEstadoItem => getCuestionarioEstadoIdentifier(cuestionarioEstadoItem)!
      );
      const cuestionarioEstadosToAdd = cuestionarioEstados.filter(cuestionarioEstadoItem => {
        const cuestionarioEstadoIdentifier = getCuestionarioEstadoIdentifier(cuestionarioEstadoItem);
        if (cuestionarioEstadoIdentifier == null || cuestionarioEstadoCollectionIdentifiers.includes(cuestionarioEstadoIdentifier)) {
          return false;
        }
        cuestionarioEstadoCollectionIdentifiers.push(cuestionarioEstadoIdentifier);
        return true;
      });
      return [...cuestionarioEstadosToAdd, ...cuestionarioEstadoCollection];
    }
    return cuestionarioEstadoCollection;
  }
}
