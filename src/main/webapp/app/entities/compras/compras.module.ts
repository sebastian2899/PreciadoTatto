import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ComprasComponent } from './list/compras.component';
import { ComprasDetailComponent } from './detail/compras-detail.component';
import { ComprasUpdateComponent } from './update/compras-update.component';
import { ComprasDeleteDialogComponent } from './delete/compras-delete-dialog.component';
import { ComprasRoutingModule } from './route/compras-routing.module';
import { NgxPaginationModule } from 'ngx-pagination';

@NgModule({
  imports: [SharedModule, ComprasRoutingModule, NgxPaginationModule],
  declarations: [ComprasComponent, ComprasDetailComponent, ComprasUpdateComponent, ComprasDeleteDialogComponent],
  entryComponents: [ComprasDeleteDialogComponent],
})
export class ComprasModule {}
