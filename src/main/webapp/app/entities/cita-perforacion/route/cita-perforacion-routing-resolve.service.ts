import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICitaPerforacion, CitaPerforacion } from '../cita-perforacion.model';
import { CitaPerforacionService } from '../service/cita-perforacion.service';

@Injectable({ providedIn: 'root' })
export class CitaPerforacionRoutingResolveService implements Resolve<ICitaPerforacion> {
  constructor(protected service: CitaPerforacionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICitaPerforacion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((citaPerforacion: HttpResponse<CitaPerforacion>) => {
          if (citaPerforacion.body) {
            return of(citaPerforacion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CitaPerforacion());
  }
}
