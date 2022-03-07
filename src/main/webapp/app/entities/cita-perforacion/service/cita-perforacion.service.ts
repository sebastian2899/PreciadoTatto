import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICitaPerforacion, getCitaPerforacionIdentifier } from '../cita-perforacion.model';
import { IMensajeValidacionCita } from 'app/entities/cita-tatto/mensaje-validacion';

export type EntityResponseType = HttpResponse<ICitaPerforacion>;
export type EntityArrayResponseType = HttpResponse<ICitaPerforacion[]>;
export type NumberType = HttpResponse<number>;
export type MensajeValidacionType = HttpResponse<IMensajeValidacionCita>;

@Injectable({ providedIn: 'root' })
export class CitaPerforacionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cita-perforacions');
  protected resourceUrlDay = this.applicationConfigService.getEndpointFor('api/cita-perforacions-day');
  protected citasPorFiltros = this.applicationConfigService.getEndpointFor('api/citaPerfoFiltro');
  protected reporteCita = this.applicationConfigService.getEndpointFor('api/generarReportePerfo');
  protected consultarTipoCita = this.applicationConfigService.getEndpointFor('api/consultarTipoCita');
  protected mensajeValidacion = this.applicationConfigService.getEndpointFor('api/mensajeValidacion');
  protected queryPorFecha = this.applicationConfigService.getEndpointFor('api/citasQueryPorFecha');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(citaPerforacion: ICitaPerforacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(citaPerforacion);
    return this.http
      .post<ICitaPerforacion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  validarCitaSave(citaPerforacion: ICitaPerforacion): Observable<MensajeValidacionType> {
    const copy = this.convertDateFromClient(citaPerforacion);
    return this.http.post<IMensajeValidacionCita>(this.mensajeValidacion, copy, { observe: 'response' });
  }

  cirasPorFiltro(resp2: any): Observable<EntityArrayResponseType> {
    const copy = this.convertDateArrayFromServer(resp2);
    return this.http
      .post<ICitaPerforacion[]>(this.citasPorFiltros, copy, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  update(citaPerforacion: ICitaPerforacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(citaPerforacion);
    return this.http
      .put<ICitaPerforacion>(`${this.resourceUrl}/${getCitaPerforacionIdentifier(citaPerforacion) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(citaPerforacion: ICitaPerforacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(citaPerforacion);
    return this.http
      .patch<ICitaPerforacion>(`${this.resourceUrl}/${getCitaPerforacionIdentifier(citaPerforacion) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICitaPerforacion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  citasPorFecha(fechaCita: string): Observable<EntityArrayResponseType> {
    return this.http.get<ICitaPerforacion[]>(`${this.queryPorFecha}/${fechaCita}`, { observe: 'response' });
  }

  consulTipoCita(id: number): Observable<NumberType> {
    return this.http.get<number>(`${this.consultarTipoCita}/${id}`, { observe: 'response' });
  }

  generarReporte(): Observable<any> {
    const httpOption = { responseType: 'arrayBuffer' as 'json' };
    return this.http.get<any>(this.reporteCita, httpOption);
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICitaPerforacion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryDay(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICitaPerforacion[]>(this.resourceUrlDay, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCitaPerforacionToCollectionIfMissing(
    citaPerforacionCollection: ICitaPerforacion[],
    ...citaPerforacionsToCheck: (ICitaPerforacion | null | undefined)[]
  ): ICitaPerforacion[] {
    const citaPerforacions: ICitaPerforacion[] = citaPerforacionsToCheck.filter(isPresent);
    if (citaPerforacions.length > 0) {
      const citaPerforacionCollectionIdentifiers = citaPerforacionCollection.map(
        citaPerforacionItem => getCitaPerforacionIdentifier(citaPerforacionItem)!
      );
      const citaPerforacionsToAdd = citaPerforacions.filter(citaPerforacionItem => {
        const citaPerforacionIdentifier = getCitaPerforacionIdentifier(citaPerforacionItem);
        if (citaPerforacionIdentifier == null || citaPerforacionCollectionIdentifiers.includes(citaPerforacionIdentifier)) {
          return false;
        }
        citaPerforacionCollectionIdentifiers.push(citaPerforacionIdentifier);
        return true;
      });
      return [...citaPerforacionsToAdd, ...citaPerforacionCollection];
    }
    return citaPerforacionCollection;
  }

  protected convertDateFromClient(citaPerforacion: ICitaPerforacion): ICitaPerforacion {
    return Object.assign({}, citaPerforacion, {
      fechaCreacion: citaPerforacion.fechaCreacion?.isValid() ? citaPerforacion.fechaCreacion.toJSON() : undefined,
      fechaCreacionInicial: citaPerforacion.fechaCreacionInicial?.isValid() ? citaPerforacion.fechaCreacionInicial.toJSON() : undefined,
      fechaCita: citaPerforacion.fechaCita?.isValid() ? citaPerforacion.fechaCita.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaCreacion = res.body.fechaCreacion ? dayjs(res.body.fechaCreacion) : undefined;
      res.body.fechaCreacionInicial = res.body.fechaCreacionInicial ? dayjs(res.body.fechaCreacionInicial) : undefined;
      res.body.fechaCita = res.body.fechaCita ? dayjs(res.body.fechaCita) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((citaPerforacion: ICitaPerforacion) => {
        citaPerforacion.fechaCreacion = citaPerforacion.fechaCreacion ? dayjs(citaPerforacion.fechaCreacion) : undefined;
        citaPerforacion.fechaCreacionInicial = citaPerforacion.fechaCreacionInicial
          ? dayjs(citaPerforacion.fechaCreacionInicial)
          : undefined;
        citaPerforacion.fechaCita = citaPerforacion.fechaCita ? dayjs(citaPerforacion.fechaCita) : undefined;
      });
    }
    return res;
  }
}
