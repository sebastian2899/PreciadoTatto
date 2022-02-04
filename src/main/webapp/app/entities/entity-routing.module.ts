import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cliente',
        data: { pageTitle: 'preciadoTattoApp.cliente.home.title' },
        loadChildren: () => import('./cliente/cliente.module').then(m => m.ClienteModule),
      },
      {
        path: 'producto',
        data: { pageTitle: 'preciadoTattoApp.producto.home.title' },
        loadChildren: () => import('./producto/producto.module').then(m => m.ProductoModule),
      },
      {
        path: 'caja-ingresos',
        data: { pageTitle: 'preciadoTattoApp.cajaIngresos.home.title' },
        loadChildren: () => import('./caja-ingresos/caja-ingresos.module').then(m => m.CajaIngresosModule),
      },
      {
        path: 'caja-tattos',
        data: { pageTitle: 'preciadoTattoApp.cajaTattos.home.title' },
        loadChildren: () => import('./caja-tattos/caja-tattos.module').then(m => m.CajaTattosModule),
      },
      {
        path: 'compras',
        data: { pageTitle: 'preciadoTattoApp.compras.home.title' },
        loadChildren: () => import('./compras/compras.module').then(m => m.ComprasModule),
      },
      {
        path: 'egreso',
        data: { pageTitle: 'preciadoTattoApp.egreso.home.title' },
        loadChildren: () => import('./egreso/egreso.module').then(m => m.EgresoModule),
      },
      {
        path: 'ventas',
        data: { pageTitle: 'preciadoTattoApp.ventas.home.title' },
        loadChildren: () => import('./ventas/ventas.module').then(m => m.VentasModule),
      },
      {
        path: 'cita-tatto',
        data: { pageTitle: 'preciadoTattoApp.citaTatto.home.title' },
        loadChildren: () => import('./cita-tatto/cita-tatto.module').then(m => m.CitaTattoModule),
      },
      {
        path: 'cita-perforacion',
        data: { pageTitle: 'preciadoTattoApp.citaPerforacion.home.title' },
        loadChildren: () => import('./cita-perforacion/cita-perforacion.module').then(m => m.CitaPerforacionModule),
      },
      {
        path: 'abono',
        data: { pageTitle: 'preciadoTattoApp.abono.home.title' },
        loadChildren: () => import('./abono/abono.module').then(m => m.AbonoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
