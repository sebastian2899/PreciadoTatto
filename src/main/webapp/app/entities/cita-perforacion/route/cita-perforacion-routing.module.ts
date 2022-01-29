import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CitaPerforacionComponent } from '../list/cita-perforacion.component';
import { CitaPerforacionDetailComponent } from '../detail/cita-perforacion-detail.component';
import { CitaPerforacionUpdateComponent } from '../update/cita-perforacion-update.component';
import { CitaPerforacionRoutingResolveService } from './cita-perforacion-routing-resolve.service';

const citaPerforacionRoute: Routes = [
  {
    path: '',
    component: CitaPerforacionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CitaPerforacionDetailComponent,
    resolve: {
      citaPerforacion: CitaPerforacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CitaPerforacionUpdateComponent,
    resolve: {
      citaPerforacion: CitaPerforacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CitaPerforacionUpdateComponent,
    resolve: {
      citaPerforacion: CitaPerforacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(citaPerforacionRoute)],
  exports: [RouterModule],
})
export class CitaPerforacionRoutingModule {}
