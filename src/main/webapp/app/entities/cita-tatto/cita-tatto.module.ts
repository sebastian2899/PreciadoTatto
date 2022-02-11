import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CitaTattoComponent } from './list/cita-tatto.component';
import { CitaTattoDetailComponent } from './detail/cita-tatto-detail.component';
import { CitaTattoUpdateComponent } from './update/cita-tatto-update.component';
import { CitaTattoDeleteDialogComponent } from './delete/cita-tatto-delete-dialog.component';
import { CitaTattoRoutingModule } from './route/cita-tatto-routing.module';
import { NgxPaginationModule } from 'ngx-pagination';

@NgModule({
  imports: [SharedModule, CitaTattoRoutingModule, NgxPaginationModule],
  declarations: [CitaTattoComponent, CitaTattoDetailComponent, CitaTattoUpdateComponent, CitaTattoDeleteDialogComponent],
  entryComponents: [CitaTattoDeleteDialogComponent],
})
export class CitaTattoModule {}
