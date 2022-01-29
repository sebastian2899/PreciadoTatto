import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICitaPerforacion, getCitaPerforacionIdentifier } from '../cita-perforacion.model';

export type EntityResponseType = HttpResponse<ICitaPerforacion>;
export type EntityArrayResponseType = HttpResponse<ICitaPerforacion[]>;

@Injectable({ providedIn: 'root' })
export class CitaPerforacionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cita-perforacions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(citaPerforacion: ICitaPerforacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(citaPerforacion);
    return this.http
      .post<ICitaPerforacion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
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

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICitaPerforacion[]>(this.resourceUrl, { params: options, observe: 'response' })
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
      fechaCita: citaPerforacion.fechaCita?.isValid() ? citaPerforacion.fechaCita.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaCreacion = res.body.fechaCreacion ? dayjs(res.body.fechaCreacion) : undefined;
      res.body.fechaCita = res.body.fechaCita ? dayjs(res.body.fechaCita) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((citaPerforacion: ICitaPerforacion) => {
        citaPerforacion.fechaCreacion = citaPerforacion.fechaCreacion ? dayjs(citaPerforacion.fechaCreacion) : undefined;
        citaPerforacion.fechaCita = citaPerforacion.fechaCita ? dayjs(citaPerforacion.fechaCita) : undefined;
      });
    }
    return res;
  }
}
