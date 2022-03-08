import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CajaIngresosComponent } from './list/caja-ingresos.component';
import { CajaIngresosDetailComponent } from './detail/caja-ingresos-detail.component';
import { CajaIngresosUpdateComponent } from './update/caja-ingresos-update.component';
import { CajaIngresosDeleteDialogComponent } from './delete/caja-ingresos-delete-dialog.component';
import { CajaIngresosRoutingModule } from './route/caja-ingresos-routing.module';
import { CajaIngFechaComponent } from './caja-ing-fecha/caja-ing-fecha.component';
import { NgxPaginationModule } from 'ngx-pagination';

@NgModule({
  imports: [SharedModule, CajaIngresosRoutingModule, NgxPaginationModule],
  declarations: [
    CajaIngresosComponent,
    CajaIngresosDetailComponent,
    CajaIngresosUpdateComponent,
    CajaIngresosDeleteDialogComponent,
    CajaIngFechaComponent,
  ],
  entryComponents: [CajaIngresosDeleteDialogComponent],
})
export class CajaIngresosModule {}
