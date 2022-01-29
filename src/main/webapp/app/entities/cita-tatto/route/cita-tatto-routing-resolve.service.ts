import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICitaTatto, CitaTatto } from '../cita-tatto.model';
import { CitaTattoService } from '../service/cita-tatto.service';

@Injectable({ providedIn: 'root' })
export class CitaTattoRoutingResolveService implements Resolve<ICitaTatto> {
  constructor(protected service: CitaTattoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICitaTatto> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((citaTatto: HttpResponse<CitaTatto>) => {
          if (citaTatto.body) {
            return of(citaTatto.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CitaTatto());
  }
}
