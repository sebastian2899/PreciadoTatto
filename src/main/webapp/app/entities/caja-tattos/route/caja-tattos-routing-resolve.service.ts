import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICajaTattos, CajaTattos } from '../caja-tattos.model';
import { CajaTattosService } from '../service/caja-tattos.service';

@Injectable({ providedIn: 'root' })
export class CajaTattosRoutingResolveService implements Resolve<ICajaTattos> {
  constructor(protected service: CajaTattosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICajaTattos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cajaTattos: HttpResponse<CajaTattos>) => {
          if (cajaTattos.body) {
            return of(cajaTattos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CajaTattos());
  }
}
