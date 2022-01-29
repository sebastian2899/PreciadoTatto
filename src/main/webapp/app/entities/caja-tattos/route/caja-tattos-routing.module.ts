import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CajaTattosComponent } from '../list/caja-tattos.component';
import { CajaTattosDetailComponent } from '../detail/caja-tattos-detail.component';
import { CajaTattosUpdateComponent } from '../update/caja-tattos-update.component';
import { CajaTattosRoutingResolveService } from './caja-tattos-routing-resolve.service';

const cajaTattosRoute: Routes = [
  {
    path: '',
    component: CajaTattosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CajaTattosDetailComponent,
    resolve: {
      cajaTattos: CajaTattosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CajaTattosUpdateComponent,
    resolve: {
      cajaTattos: CajaTattosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CajaTattosUpdateComponent,
    resolve: {
      cajaTattos: CajaTattosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cajaTattosRoute)],
  exports: [RouterModule],
})
export class CajaTattosRoutingModule {}
