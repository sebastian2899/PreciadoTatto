import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CitaTattoComponent } from '../list/cita-tatto.component';
import { CitaTattoDetailComponent } from '../detail/cita-tatto-detail.component';
import { CitaTattoUpdateComponent } from '../update/cita-tatto-update.component';
import { CitaTattoRoutingResolveService } from './cita-tatto-routing-resolve.service';

const citaTattoRoute: Routes = [
  {
    path: '',
    component: CitaTattoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CitaTattoDetailComponent,
    resolve: {
      citaTatto: CitaTattoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CitaTattoUpdateComponent,
    resolve: {
      citaTatto: CitaTattoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CitaTattoUpdateComponent,
    resolve: {
      citaTatto: CitaTattoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(citaTattoRoute)],
  exports: [RouterModule],
})
export class CitaTattoRoutingModule {}
