import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGaleriaFotos, getGaleriaFotosIdentifier } from '../galeria-fotos.model';

export type EntityResponseType = HttpResponse<IGaleriaFotos>;
export type EntityArrayResponseType = HttpResponse<IGaleriaFotos[]>;

@Injectable({ providedIn: 'root' })
export class GaleriaFotosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/galeria-fotos');
  protected galeriaFiltros = this.applicationConfigService.getEndpointFor('api/galeriaFiltros');
  protected galeriaPorOrden = this.applicationConfigService.getEndpointFor('api/galeriaSelect');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(galeriaFotos: IGaleriaFotos): Observable<EntityResponseType> {
    return this.http.post<IGaleriaFotos>(this.resourceUrl, galeriaFotos, { observe: 'response' });
  }

  update(galeriaFotos: IGaleriaFotos): Observable<EntityResponseType> {
    return this.http.put<IGaleriaFotos>(`${this.resourceUrl}/${getGaleriaFotosIdentifier(galeriaFotos) as number}`, galeriaFotos, {
      observe: 'response',
    });
  }

  partialUpdate(galeriaFotos: IGaleriaFotos): Observable<EntityResponseType> {
    return this.http.patch<IGaleriaFotos>(`${this.resourceUrl}/${getGaleriaFotosIdentifier(galeriaFotos) as number}`, galeriaFotos, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGaleriaFotos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  galeriaOrden(tipOrden: string): Observable<EntityArrayResponseType> {
    return this.http.get<IGaleriaFotos[]>(`${this.galeriaPorOrden}/${tipOrden}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGaleriaFotos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  galeriaFiltro(disenio: IGaleriaFotos): Observable<EntityArrayResponseType> {
    return this.http.post<IGaleriaFotos[]>(this.galeriaFiltros, disenio, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGaleriaFotosToCollectionIfMissing(
    galeriaFotosCollection: IGaleriaFotos[],
    ...galeriaFotosToCheck: (IGaleriaFotos | null | undefined)[]
  ): IGaleriaFotos[] {
    const galeriaFotos: IGaleriaFotos[] = galeriaFotosToCheck.filter(isPresent);
    if (galeriaFotos.length > 0) {
      const galeriaFotosCollectionIdentifiers = galeriaFotosCollection.map(
        galeriaFotosItem => getGaleriaFotosIdentifier(galeriaFotosItem)!
      );
      const galeriaFotosToAdd = galeriaFotos.filter(galeriaFotosItem => {
        const galeriaFotosIdentifier = getGaleriaFotosIdentifier(galeriaFotosItem);
        if (galeriaFotosIdentifier == null || galeriaFotosCollectionIdentifiers.includes(galeriaFotosIdentifier)) {
          return false;
        }
        galeriaFotosCollectionIdentifiers.push(galeriaFotosIdentifier);
        return true;
      });
      return [...galeriaFotosToAdd, ...galeriaFotosCollection];
    }
    return galeriaFotosCollection;
  }
}
