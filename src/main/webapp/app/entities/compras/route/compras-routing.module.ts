import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ComprasComponent } from '../list/compras.component';
import { ComprasDetailComponent } from '../detail/compras-detail.component';
import { ComprasUpdateComponent } from '../update/compras-update.component';
import { ComprasRoutingResolveService } from './compras-routing-resolve.service';

const comprasRoute: Routes = [
  {
    path: '',
    component: ComprasComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ComprasDetailComponent,
    resolve: {
      compras: ComprasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ComprasUpdateComponent,
    resolve: {
      compras: ComprasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ComprasUpdateComponent,
    resolve: {
      compras: ComprasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(comprasRoute)],
  exports: [RouterModule],
})
export class ComprasRoutingModule {}
