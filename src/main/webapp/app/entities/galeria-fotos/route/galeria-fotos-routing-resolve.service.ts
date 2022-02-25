import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGaleriaFotos, GaleriaFotos } from '../galeria-fotos.model';
import { GaleriaFotosService } from '../service/galeria-fotos.service';

@Injectable({ providedIn: 'root' })
export class GaleriaFotosRoutingResolveService implements Resolve<IGaleriaFotos> {
  constructor(protected service: GaleriaFotosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGaleriaFotos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((galeriaFotos: HttpResponse<GaleriaFotos>) => {
          if (galeriaFotos.body) {
            return of(galeriaFotos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GaleriaFotos());
  }
}
