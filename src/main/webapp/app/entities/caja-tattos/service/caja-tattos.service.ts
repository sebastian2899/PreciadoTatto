import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICajaTattos, getCajaTattosIdentifier } from '../caja-tattos.model';
import { ICajaFechasTattoos } from '../caja-fechas.model';
import * as dayjs from 'dayjs';
import { map } from 'rxjs/operators';

export type EntityResponseType = HttpResponse<ICajaTattos>;
export type EntityArrayResponseType = HttpResponse<ICajaTattos[]>;
export type NumberType = HttpResponse<number>;
export type EntityFechasType = HttpResponse<ICajaFechasTattoos>;

@Injectable({ providedIn: 'root' })
export class CajaTattosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/caja-tattos');
  protected URLconsultarValorVendidoDia = this.applicationConfigService.getEndpointFor('api/consultarValorDia');
  protected URLconsultarCajaFechas = this.applicationConfigService.getEndpointFor('api/registro-caja-fecha');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cajaTattos: ICajaTattos): Observable<EntityResponseType> {
    return this.http.post<ICajaTattos>(this.resourceUrl, cajaTattos, { observe: 'response' });
  }

  update(cajaTattos: ICajaTattos): Observable<EntityResponseType> {
    return this.http.put<ICajaTattos>(`${this.resourceUrl}/${getCajaTattosIdentifier(cajaTattos) as number}`, cajaTattos, {
      observe: 'response',
    });
  }

  partialUpdate(cajaTattos: ICajaTattos): Observable<EntityResponseType> {
    return this.http.patch<ICajaTattos>(`${this.resourceUrl}/${getCajaTattosIdentifier(cajaTattos) as number}`, cajaTattos, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICajaTattos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  valorDia(): Observable<NumberType> {
    return this.http.get<number>(this.URLconsultarValorVendidoDia, { observe: 'response' });
  }

  cajaPorFecha(fechaIni: string, fechaFin: string): Observable<EntityFechasType> {
    return this.http
      .get<ICajaFechasTattoos>(`${this.URLconsultarCajaFechas}/${fechaIni}/${fechaFin}`, { observe: 'response' })
      .pipe(map((res: EntityFechasType) => res));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICajaTattos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCajaTattosToCollectionIfMissing(
    cajaTattosCollection: ICajaTattos[],
    ...cajaTattosToCheck: (ICajaTattos | null | undefined)[]
  ): ICajaTattos[] {
    const cajaTattos: ICajaTattos[] = cajaTattosToCheck.filter(isPresent);
    if (cajaTattos.length > 0) {
      const cajaTattosCollectionIdentifiers = cajaTattosCollection.map(cajaTattosItem => getCajaTattosIdentifier(cajaTattosItem)!);
      const cajaTattosToAdd = cajaTattos.filter(cajaTattosItem => {
        const cajaTattosIdentifier = getCajaTattosIdentifier(cajaTattosItem);
        if (cajaTattosIdentifier == null || cajaTattosCollectionIdentifiers.includes(cajaTattosIdentifier)) {
          return false;
        }
        cajaTattosCollectionIdentifiers.push(cajaTattosIdentifier);
        return true;
      });
      return [...cajaTattosToAdd, ...cajaTattosCollection];
    }
    return cajaTattosCollection;
  }

  protected convertDateFromClient(cajaTattoo: ICajaTattos): ICajaTattos {
    return Object.assign({}, cajaTattoo, {
      fechaCreacion: cajaTattoo.fechaCreacion?.isValid() ? cajaTattoo.fechaCreacion.toJSON() : undefined,
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
      res.body.forEach((cajaTattoo: ICajaTattos) => {
        cajaTattoo.fechaCreacion = cajaTattoo.fechaCreacion ? dayjs(cajaTattoo.fechaCreacion) : undefined;
      });
    }
    return res;
  }
}
