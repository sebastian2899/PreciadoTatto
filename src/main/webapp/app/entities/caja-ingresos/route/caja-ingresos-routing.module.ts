import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CajaIngresosComponent } from '../list/caja-ingresos.component';
import { CajaIngresosDetailComponent } from '../detail/caja-ingresos-detail.component';
import { CajaIngresosUpdateComponent } from '../update/caja-ingresos-update.component';
import { CajaIngresosRoutingResolveService } from './caja-ingresos-routing-resolve.service';
import { CajaIngFechaComponent } from '../caja-ing-fecha/caja-ing-fecha.component';

const cajaIngresosRoute: Routes = [
  {
    path: '',
    component: CajaIngresosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CajaIngresosDetailComponent,
    resolve: {
      cajaIngresos: CajaIngresosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CajaIngresosUpdateComponent,
    resolve: {
      cajaIngresos: CajaIngresosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CajaIngresosUpdateComponent,
    resolve: {
      cajaIngresos: CajaIngresosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'caja-ing-fecha',
    component: CajaIngFechaComponent,
    resolve: {
      cajaIngresos: CajaIngresosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cajaIngresosRoute)],
  exports: [RouterModule],
})
export class CajaIngresosRoutingModule {}
