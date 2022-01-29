import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CajaTattosComponent } from './list/caja-tattos.component';
import { CajaTattosDetailComponent } from './detail/caja-tattos-detail.component';
import { CajaTattosUpdateComponent } from './update/caja-tattos-update.component';
import { CajaTattosDeleteDialogComponent } from './delete/caja-tattos-delete-dialog.component';
import { CajaTattosRoutingModule } from './route/caja-tattos-routing.module';

@NgModule({
  imports: [SharedModule, CajaTattosRoutingModule],
  declarations: [CajaTattosComponent, CajaTattosDetailComponent, CajaTattosUpdateComponent, CajaTattosDeleteDialogComponent],
  entryComponents: [CajaTattosDeleteDialogComponent],
})
export class CajaTattosModule {}
