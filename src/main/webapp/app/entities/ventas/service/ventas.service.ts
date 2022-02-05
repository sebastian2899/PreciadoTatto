import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVentas, getVentasIdentifier } from '../ventas.model';
import * as dayjs from 'dayjs';
import { map } from 'rxjs/operators';

export type EntityResponseType = HttpResponse<IVentas>;
export type EntityArrayResponseType = HttpResponse<IVentas[]>;

@Injectable({ providedIn: 'root' })
export class VentasService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ventas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ventas: IVentas): Observable<EntityResponseType> {
    return this.http.post<IVentas>(this.resourceUrl, ventas, { observe: 'response' });
  }

  update(ventas: IVentas): Observable<EntityResponseType> {
    return this.http.put<IVentas>(`${this.resourceUrl}/${getVentasIdentifier(ventas) as number}`, ventas, { observe: 'response' });
  }

  partialUpdate(ventas: IVentas): Observable<EntityResponseType> {
    return this.http.patch<IVentas>(`${this.resourceUrl}/${getVentasIdentifier(ventas) as number}`, ventas, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVentas>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVentas[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVentasToCollectionIfMissing(ventasCollection: IVentas[], ...ventasToCheck: (IVentas | null | undefined)[]): IVentas[] {
    const ventas: IVentas[] = ventasToCheck.filter(isPresent);
    if (ventas.length > 0) {
      const ventasCollectionIdentifiers = ventasCollection.map(ventasItem => getVentasIdentifier(ventasItem)!);
      const ventasToAdd = ventas.filter(ventasItem => {
        const ventasIdentifier = getVentasIdentifier(ventasItem);
        if (ventasIdentifier == null || ventasCollectionIdentifiers.includes(ventasIdentifier)) {
          return false;
        }
        ventasCollectionIdentifiers.push(ventasIdentifier);
        return true;
      });
      return [...ventasToAdd, ...ventasCollection];
    }
    return ventasCollection;
  }

  protected convertDateFromClient(ventas: IVentas): IVentas {
    return Object.assign({}, ventas, {
      fechaCreacion: ventas.fechaCreacion?.isValid() ? ventas.fechaCreacion.toJSON() : undefined,
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
      res.body.forEach((ventas: IVentas) => {
        ventas.fechaCreacion = ventas.fechaCreacion ? dayjs(ventas.fechaCreacion) : undefined;
      });
    }
    return res;
  }
}
