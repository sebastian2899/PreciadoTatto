import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAbono, Abono } from '../abono.model';
import { AbonoService } from '../service/abono.service';

@Injectable({ providedIn: 'root' })
export class AbonoRoutingResolveService implements Resolve<IAbono> {
  constructor(protected service: AbonoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAbono> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((abono: HttpResponse<Abono>) => {
          if (abono.body) {
            return of(abono.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Abono());
  }
}
