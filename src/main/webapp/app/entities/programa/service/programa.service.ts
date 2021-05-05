import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrograma, getProgramaIdentifier } from '../programa.model';

export type EntityResponseType = HttpResponse<IPrograma>;
export type EntityArrayResponseType = HttpResponse<IPrograma[]>;

@Injectable({ providedIn: 'root' })
export class ProgramaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/programas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(programa: IPrograma): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(programa);
    return this.http
      .post<IPrograma>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(programa: IPrograma): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(programa);
    return this.http
      .put<IPrograma>(`${this.resourceUrl}/${getProgramaIdentifier(programa) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(programa: IPrograma): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(programa);
    return this.http
      .patch<IPrograma>(`${this.resourceUrl}/${getProgramaIdentifier(programa) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPrograma>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrograma[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProgramaToCollectionIfMissing(programaCollection: IPrograma[], ...programasToCheck: (IPrograma | null | undefined)[]): IPrograma[] {
    const programas: IPrograma[] = programasToCheck.filter(isPresent);
    if (programas.length > 0) {
      const programaCollectionIdentifiers = programaCollection.map(programaItem => getProgramaIdentifier(programaItem)!);
      const programasToAdd = programas.filter(programaItem => {
        const programaIdentifier = getProgramaIdentifier(programaItem);
        if (programaIdentifier == null || programaCollectionIdentifiers.includes(programaIdentifier)) {
          return false;
        }
        programaCollectionIdentifiers.push(programaIdentifier);
        return true;
      });
      return [...programasToAdd, ...programaCollection];
    }
    return programaCollection;
  }

  protected convertDateFromClient(programa: IPrograma): IPrograma {
    return Object.assign({}, programa, {
      fechaRegistro: programa.fechaRegistro?.isValid() ? programa.fechaRegistro.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaRegistro = res.body.fechaRegistro ? dayjs(res.body.fechaRegistro) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((programa: IPrograma) => {
        programa.fechaRegistro = programa.fechaRegistro ? dayjs(programa.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
