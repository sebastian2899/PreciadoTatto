import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VentasComponent } from '../list/ventas.component';
import { VentasDetailComponent } from '../detail/ventas-detail.component';
import { VentasUpdateComponent } from '../update/ventas-update.component';
import { VentasRoutingResolveService } from './ventas-routing-resolve.service';

const ventasRoute: Routes = [
  {
    path: '',
    component: VentasComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VentasDetailComponent,
    resolve: {
      ventas: VentasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VentasUpdateComponent,
    resolve: {
      ventas: VentasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VentasUpdateComponent,
    resolve: {
      ventas: VentasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ventasRoute)],
  exports: [RouterModule],
})
export class VentasRoutingModule {}
