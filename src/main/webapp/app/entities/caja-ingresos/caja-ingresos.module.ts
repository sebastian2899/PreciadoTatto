import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CajaIngresosComponent } from './list/caja-ingresos.component';
import { CajaIngresosDetailComponent } from './detail/caja-ingresos-detail.component';
import { CajaIngresosUpdateComponent } from './update/caja-ingresos-update.component';
import { CajaIngresosDeleteDialogComponent } from './delete/caja-ingresos-delete-dialog.component';
import { CajaIngresosRoutingModule } from './route/caja-ingresos-routing.module';

@NgModule({
  imports: [SharedModule, CajaIngresosRoutingModule],
  declarations: [CajaIngresosComponent, CajaIngresosDetailComponent, CajaIngresosUpdateComponent, CajaIngresosDeleteDialogComponent],
  entryComponents: [CajaIngresosDeleteDialogComponent],
})
export class CajaIngresosModule {}
