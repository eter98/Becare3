import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedicamento, getMedicamentoIdentifier } from '../medicamento.model';

export type EntityResponseType = HttpResponse<IMedicamento>;
export type EntityArrayResponseType = HttpResponse<IMedicamento[]>;

@Injectable({ providedIn: 'root' })
export class MedicamentoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/medicamentos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(medicamento: IMedicamento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicamento);
    return this.http
      .post<IMedicamento>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(medicamento: IMedicamento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicamento);
    return this.http
      .put<IMedicamento>(`${this.resourceUrl}/${getMedicamentoIdentifier(medicamento) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(medicamento: IMedicamento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicamento);
    return this.http
      .patch<IMedicamento>(`${this.resourceUrl}/${getMedicamentoIdentifier(medicamento) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMedicamento>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMedicamento[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMedicamentoToCollectionIfMissing(
    medicamentoCollection: IMedicamento[],
    ...medicamentosToCheck: (IMedicamento | null | undefined)[]
  ): IMedicamento[] {
    const medicamentos: IMedicamento[] = medicamentosToCheck.filter(isPresent);
    if (medicamentos.length > 0) {
      const medicamentoCollectionIdentifiers = medicamentoCollection.map(medicamentoItem => getMedicamentoIdentifier(medicamentoItem)!);
      const medicamentosToAdd = medicamentos.filter(medicamentoItem => {
        const medicamentoIdentifier = getMedicamentoIdentifier(medicamentoItem);
        if (medicamentoIdentifier == null || medicamentoCollectionIdentifiers.includes(medicamentoIdentifier)) {
          return false;
        }
        medicamentoCollectionIdentifiers.push(medicamentoIdentifier);
        return true;
      });
      return [...medicamentosToAdd, ...medicamentoCollection];
    }
    return medicamentoCollection;
  }

  protected convertDateFromClient(medicamento: IMedicamento): IMedicamento {
    return Object.assign({}, medicamento, {
      fechaIngreso: medicamento.fechaIngreso?.isValid() ? medicamento.fechaIngreso.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaIngreso = res.body.fechaIngreso ? dayjs(res.body.fechaIngreso) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((medicamento: IMedicamento) => {
        medicamento.fechaIngreso = medicamento.fechaIngreso ? dayjs(medicamento.fechaIngreso) : undefined;
      });
    }
    return res;
  }
}
