import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICitaTatto, getCitaTattoIdentifier } from '../cita-tatto.model';

export type EntityResponseType = HttpResponse<ICitaTatto>;
export type EntityArrayResponseType = HttpResponse<ICitaTatto[]>;

@Injectable({ providedIn: 'root' })
export class CitaTattoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cita-tattos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(citaTatto: ICitaTatto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(citaTatto);
    return this.http
      .post<ICitaTatto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(citaTatto: ICitaTatto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(citaTatto);
    return this.http
      .put<ICitaTatto>(`${this.resourceUrl}/${getCitaTattoIdentifier(citaTatto) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(citaTatto: ICitaTatto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(citaTatto);
    return this.http
      .patch<ICitaTatto>(`${this.resourceUrl}/${getCitaTattoIdentifier(citaTatto) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICitaTatto>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICitaTatto[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCitaTattoToCollectionIfMissing(
    citaTattoCollection: ICitaTatto[],
    ...citaTattosToCheck: (ICitaTatto | null | undefined)[]
  ): ICitaTatto[] {
    const citaTattos: ICitaTatto[] = citaTattosToCheck.filter(isPresent);
    if (citaTattos.length > 0) {
      const citaTattoCollectionIdentifiers = citaTattoCollection.map(citaTattoItem => getCitaTattoIdentifier(citaTattoItem)!);
      const citaTattosToAdd = citaTattos.filter(citaTattoItem => {
        const citaTattoIdentifier = getCitaTattoIdentifier(citaTattoItem);
        if (citaTattoIdentifier == null || citaTattoCollectionIdentifiers.includes(citaTattoIdentifier)) {
          return false;
        }
        citaTattoCollectionIdentifiers.push(citaTattoIdentifier);
        return true;
      });
      return [...citaTattosToAdd, ...citaTattoCollection];
    }
    return citaTattoCollection;
  }

  protected convertDateFromClient(citaTatto: ICitaTatto): ICitaTatto {
    return Object.assign({}, citaTatto, {
      fechaCreacion: citaTatto.fechaCreacion?.isValid() ? citaTatto.fechaCreacion.toJSON() : undefined,
      fechaCita: citaTatto.fechaCita?.isValid() ? citaTatto.fechaCita.toJSON() : undefined,
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
      res.body.forEach((citaTatto: ICitaTatto) => {
        citaTatto.fechaCreacion = citaTatto.fechaCreacion ? dayjs(citaTatto.fechaCreacion) : undefined;
        citaTatto.fechaCita = citaTatto.fechaCita ? dayjs(citaTatto.fechaCita) : undefined;
      });
    }
    return res;
  }
}