import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICajaIngresos, getCajaIngresosIdentifier } from '../caja-ingresos.model';
import { ICajaFechaIngresos } from '../caja-fechas.';

export type EntityResponseType = HttpResponse<ICajaIngresos>;
export type EntityArrayResponseType = HttpResponse<ICajaIngresos[]>;
export type NumberType = HttpResponse<number>;
export type CajaFechasType = HttpResponse<ICajaFechaIngresos>;

@Injectable({ providedIn: 'root' })
export class CajaIngresosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/caja-ingresos');
  protected valorcajaDia = this.applicationConfigService.getEndpointFor('api/caja-ingresosDia');
  protected cajaFechas = this.applicationConfigService.getEndpointFor('api/cajaIngresosFecha');
  protected generarReporte = this.applicationConfigService.getEndpointFor('api/generarReporteIngreso');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cajaIngresos: ICajaIngresos): Observable<EntityResponseType> {
    return this.http.post<ICajaIngresos>(this.resourceUrl, cajaIngresos, { observe: 'response' });
  }

  update(cajaIngresos: ICajaIngresos): Observable<EntityResponseType> {
    return this.http.put<ICajaIngresos>(`${this.resourceUrl}/${getCajaIngresosIdentifier(cajaIngresos) as number}`, cajaIngresos, {
      observe: 'response',
    });
  }

  partialUpdate(cajaIngresos: ICajaIngresos): Observable<EntityResponseType> {
    return this.http.patch<ICajaIngresos>(`${this.resourceUrl}/${getCajaIngresosIdentifier(cajaIngresos) as number}`, cajaIngresos, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICajaIngresos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  generarReport(): Observable<any> {
    const httpOption = { responseType: 'arrayBuffer' as 'json' };
    return this.http.get<any>(this.generarReporte, httpOption);
  }

  cajaDia(): Observable<NumberType> {
    return this.http.get<number>(this.valorcajaDia, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICajaIngresos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  cajaIngFechas(fechaInicio: string, fechaFin: string): Observable<CajaFechasType> {
    return this.http.get<ICajaFechaIngresos>(`${this.cajaFechas}/${fechaInicio}/${fechaFin}`, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCajaIngresosToCollectionIfMissing(
    cajaIngresosCollection: ICajaIngresos[],
    ...cajaIngresosToCheck: (ICajaIngresos | null | undefined)[]
  ): ICajaIngresos[] {
    const cajaIngresos: ICajaIngresos[] = cajaIngresosToCheck.filter(isPresent);
    if (cajaIngresos.length > 0) {
      const cajaIngresosCollectionIdentifiers = cajaIngresosCollection.map(
        cajaIngresosItem => getCajaIngresosIdentifier(cajaIngresosItem)!
      );
      const cajaIngresosToAdd = cajaIngresos.filter(cajaIngresosItem => {
        const cajaIngresosIdentifier = getCajaIngresosIdentifier(cajaIngresosItem);
        if (cajaIngresosIdentifier == null || cajaIngresosCollectionIdentifiers.includes(cajaIngresosIdentifier)) {
          return false;
        }
        cajaIngresosCollectionIdentifiers.push(cajaIngresosIdentifier);
        return true;
      });
      return [...cajaIngresosToAdd, ...cajaIngresosCollection];
    }
    return cajaIngresosCollection;
  }
}
