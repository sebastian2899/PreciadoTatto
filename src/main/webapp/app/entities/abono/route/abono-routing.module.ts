import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AbonoComponent } from '../list/abono.component';
import { AbonoDetailComponent } from '../detail/abono-detail.component';
import { AbonoUpdateComponent } from '../update/abono-update.component';
import { AbonoRoutingResolveService } from './abono-routing-resolve.service';

const abonoRoute: Routes = [
  {
    path: '',
    component: AbonoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AbonoDetailComponent,
    resolve: {
      abono: AbonoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AbonoUpdateComponent,
    resolve: {
      abono: AbonoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AbonoUpdateComponent,
    resolve: {
      abono: AbonoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(abonoRoute)],
  exports: [RouterModule],
})
export class AbonoRoutingModule {}
