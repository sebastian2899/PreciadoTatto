import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VentasComponent } from './list/ventas.component';
import { VentasDetailComponent } from './detail/ventas-detail.component';
import { VentasUpdateComponent } from './update/ventas-update.component';
import { VentasDeleteDialogComponent } from './delete/ventas-delete-dialog.component';
import { VentasRoutingModule } from './route/ventas-routing.module';

@NgModule({
  imports: [SharedModule, VentasRoutingModule],
  declarations: [VentasComponent, VentasDetailComponent, VentasUpdateComponent, VentasDeleteDialogComponent],
  entryComponents: [VentasDeleteDialogComponent],
})
export class VentasModule {}
