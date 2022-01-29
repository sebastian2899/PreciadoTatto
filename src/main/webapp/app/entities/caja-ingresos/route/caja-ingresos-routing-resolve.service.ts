import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICajaIngresos, CajaIngresos } from '../caja-ingresos.model';
import { CajaIngresosService } from '../service/caja-ingresos.service';

@Injectable({ providedIn: 'root' })
export class CajaIngresosRoutingResolveService implements Resolve<ICajaIngresos> {
  constructor(protected service: CajaIngresosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICajaIngresos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cajaIngresos: HttpResponse<CajaIngresos>) => {
          if (cajaIngresos.body) {
            return of(cajaIngresos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CajaIngresos());
  }
}
