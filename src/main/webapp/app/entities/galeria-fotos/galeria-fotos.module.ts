import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GaleriaFotosComponent } from './list/galeria-fotos.component';
import { GaleriaFotosDetailComponent } from './detail/galeria-fotos-detail.component';
import { GaleriaFotosUpdateComponent } from './update/galeria-fotos-update.component';
import { GaleriaFotosDeleteDialogComponent } from './delete/galeria-fotos-delete-dialog.component';
import { GaleriaFotosRoutingModule } from './route/galeria-fotos-routing.module';
import { NgxPaginationModule } from 'ngx-pagination';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  imports: [SharedModule, GaleriaFotosRoutingModule, NgxPaginationModule, MatPaginatorModule, MatButtonModule, MatIconModule],
  declarations: [GaleriaFotosComponent, GaleriaFotosDetailComponent, GaleriaFotosUpdateComponent, GaleriaFotosDeleteDialogComponent],
  entryComponents: [GaleriaFotosDeleteDialogComponent],
})
export class GaleriaFotosModule {}
