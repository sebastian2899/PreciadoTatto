import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GaleriaFotosComponent } from '../list/galeria-fotos.component';
import { GaleriaFotosDetailComponent } from '../detail/galeria-fotos-detail.component';
import { GaleriaFotosUpdateComponent } from '../update/galeria-fotos-update.component';
import { GaleriaFotosRoutingResolveService } from './galeria-fotos-routing-resolve.service';

const galeriaFotosRoute: Routes = [
  {
    path: '',
    component: GaleriaFotosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GaleriaFotosDetailComponent,
    resolve: {
      galeriaFotos: GaleriaFotosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GaleriaFotosUpdateComponent,
    resolve: {
      galeriaFotos: GaleriaFotosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GaleriaFotosUpdateComponent,
    resolve: {
      galeriaFotos: GaleriaFotosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(galeriaFotosRoute)],
  exports: [RouterModule],
})
export class GaleriaFotosRoutingModule {}
