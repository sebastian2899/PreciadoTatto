import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CitaPerforacionComponent } from './list/cita-perforacion.component';
import { CitaPerforacionDetailComponent } from './detail/cita-perforacion-detail.component';
import { CitaPerforacionUpdateComponent } from './update/cita-perforacion-update.component';
import { CitaPerforacionDeleteDialogComponent } from './delete/cita-perforacion-delete-dialog.component';
import { CitaPerforacionRoutingModule } from './route/cita-perforacion-routing.module';
import { NgxPaginationModule } from 'ngx-pagination';

@NgModule({
  imports: [SharedModule, CitaPerforacionRoutingModule, NgxPaginationModule],
  declarations: [
    CitaPerforacionComponent,
    CitaPerforacionDetailComponent,
    CitaPerforacionUpdateComponent,
    CitaPerforacionDeleteDialogComponent,
  ],
  entryComponents: [CitaPerforacionDeleteDialogComponent],
})
export class CitaPerforacionModule {}
