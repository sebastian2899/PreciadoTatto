import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompras, getComprasIdentifier } from '../compras.model';

export type EntityResponseType = HttpResponse<ICompras>;
export type EntityArrayResponseType = HttpResponse<ICompras[]>;

@Injectable({ providedIn: 'root' })
export class ComprasService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/compras');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(compras: ICompras): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compras);
    return this.http
      .post<ICompras>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(compras: ICompras): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compras);
    return this.http
      .put<ICompras>(`${this.resourceUrl}/${getComprasIdentifier(compras) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(compras: ICompras): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compras);
    return this.http
      .patch<ICompras>(`${this.resourceUrl}/${getComprasIdentifier(compras) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICompras>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICompras[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addComprasToCollectionIfMissing(comprasCollection: ICompras[], ...comprasToCheck: (ICompras | null | undefined)[]): ICompras[] {
    const compras: ICompras[] = comprasToCheck.filter(isPresent);
    if (compras.length > 0) {
      const comprasCollectionIdentifiers = comprasCollection.map(comprasItem => getComprasIdentifier(comprasItem)!);
      const comprasToAdd = compras.filter(comprasItem => {
        const comprasIdentifier = getComprasIdentifier(comprasItem);
        if (comprasIdentifier == null || comprasCollectionIdentifiers.includes(comprasIdentifier)) {
          return false;
        }
        comprasCollectionIdentifiers.push(comprasIdentifier);
        return true;
      });
      return [...comprasToAdd, ...comprasCollection];
    }
    return comprasCollection;
  }

  protected convertDateFromClient(compras: ICompras): ICompras {
    return Object.assign({}, compras, {
      fechaCreacion: compras.fechaCreacion?.isValid() ? compras.fechaCreacion.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaCreacion = res.body.fechaCreacion ? dayjs(res.body.fechaCreacion) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((compras: ICompras) => {
        compras.fechaCreacion = compras.fechaCreacion ? dayjs(compras.fechaCreacion) : undefined;
      });
    }
    return res;
  }
}
