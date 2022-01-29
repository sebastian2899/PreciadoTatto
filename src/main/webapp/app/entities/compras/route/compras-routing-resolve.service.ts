import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompras, Compras } from '../compras.model';
import { ComprasService } from '../service/compras.service';

@Injectable({ providedIn: 'root' })
export class ComprasRoutingResolveService implements Resolve<ICompras> {
  constructor(protected service: ComprasService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompras> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((compras: HttpResponse<Compras>) => {
          if (compras.body) {
            return of(compras.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Compras());
  }
}
