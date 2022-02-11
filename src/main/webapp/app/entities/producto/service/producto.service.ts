import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProducto, getProductoIdentifier } from '../producto.model';

export type EntityResponseType = HttpResponse<IProducto>;
export type EntityArrayResponseType = HttpResponse<IProducto[]>;
export type NumberType = HttpResponse<number>;

@Injectable({ providedIn: 'root' })
export class ProductoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/productos');
  protected productosDisp = this.applicationConfigService.getEndpointFor('api/productosDisponibles');
  protected totalProduc = this.applicationConfigService.getEndpointFor('api/totalProductos');
  protected totalVent = this.applicationConfigService.getEndpointFor('api/totalVentas');
  protected totalCompr = this.applicationConfigService.getEndpointFor('api/totalCompras');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(producto: IProducto): Observable<EntityResponseType> {
    return this.http.post<IProducto>(this.resourceUrl, producto, { observe: 'response' });
  }

  update(producto: IProducto): Observable<EntityResponseType> {
    return this.http.put<IProducto>(`${this.resourceUrl}/${getProductoIdentifier(producto) as number}`, producto, { observe: 'response' });
  }

  partialUpdate(producto: IProducto): Observable<EntityResponseType> {
    return this.http.patch<IProducto>(`${this.resourceUrl}/${getProductoIdentifier(producto) as number}`, producto, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProducto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  totalProducto(): Observable<NumberType> {
    return this.http.get<number>(this.totalProduc, { observe: 'response' });
  }

  totalVentas(): Observable<NumberType> {
    return this.http.get<number>(this.totalVent, { observe: 'response' });
  }

  totalCompras(): Observable<NumberType> {
    return this.http.get<number>(this.totalCompr, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProducto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  disponibles(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProducto[]>(this.productosDisp, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProductoToCollectionIfMissing(productoCollection: IProducto[], ...productosToCheck: (IProducto | null | undefined)[]): IProducto[] {
    const productos: IProducto[] = productosToCheck.filter(isPresent);
    if (productos.length > 0) {
      const productoCollectionIdentifiers = productoCollection.map(productoItem => getProductoIdentifier(productoItem)!);
      const productosToAdd = productos.filter(productoItem => {
        const productoIdentifier = getProductoIdentifier(productoItem);
        if (productoIdentifier == null || productoCollectionIdentifiers.includes(productoIdentifier)) {
          return false;
        }
        productoCollectionIdentifiers.push(productoIdentifier);
        return true;
      });
      return [...productosToAdd, ...productoCollection];
    }
    return productoCollection;
  }
}
