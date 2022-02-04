import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAbono, getAbonoIdentifier } from '../abono.model';

export type EntityResponseType = HttpResponse<IAbono>;
export type EntityArrayResponseType = HttpResponse<IAbono[]>;
export type NumberType = HttpResponse<number>;

@Injectable({ providedIn: 'root' })
export class AbonoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/abonos');
  protected valorDeudaUrl = this.applicationConfigService.getEndpointFor('api/valorDeuda');
  protected abonoPorCita = this.applicationConfigService.getEndpointFor('api/abonosPorCita');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(abono: IAbono): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(abono);
    return this.http
      .post<IAbono>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  valorDeuda(id: number): Observable<NumberType> {
    return this.http.get<number>(`${this.valorDeudaUrl}/${id}`, { observe: 'response' });
  }

  update(abono: IAbono): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(abono);
    return this.http
      .put<IAbono>(`${this.resourceUrl}/${getAbonoIdentifier(abono) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(abono: IAbono): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(abono);
    return this.http
      .patch<IAbono>(`${this.resourceUrl}/${getAbonoIdentifier(abono) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAbono>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAbono[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryPorCita(idCita: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<IAbono[]>(`${this.abonoPorCita}/${idCita}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAbonoToCollectionIfMissing(abonoCollection: IAbono[], ...abonosToCheck: (IAbono | null | undefined)[]): IAbono[] {
    const abonos: IAbono[] = abonosToCheck.filter(isPresent);
    if (abonos.length > 0) {
      const abonoCollectionIdentifiers = abonoCollection.map(abonoItem => getAbonoIdentifier(abonoItem)!);
      const abonosToAdd = abonos.filter(abonoItem => {
        const abonoIdentifier = getAbonoIdentifier(abonoItem);
        if (abonoIdentifier == null || abonoCollectionIdentifiers.includes(abonoIdentifier)) {
          return false;
        }
        abonoCollectionIdentifiers.push(abonoIdentifier);
        return true;
      });
      return [...abonosToAdd, ...abonoCollection];
    }
    return abonoCollection;
  }

  protected convertDateFromClient(abono: IAbono): IAbono {
    return Object.assign({}, abono, {
      fechaAbono: abono.fechaAbono?.isValid() ? abono.fechaAbono.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaAbono = res.body.fechaAbono ? dayjs(res.body.fechaAbono) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((abono: IAbono) => {
        abono.fechaAbono = abono.fechaAbono ? dayjs(abono.fechaAbono) : undefined;
      });
    }
    return res;
  }
}
