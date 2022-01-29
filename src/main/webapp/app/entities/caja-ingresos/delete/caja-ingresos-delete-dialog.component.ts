import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICajaIngresos } from '../caja-ingresos.model';
import { CajaIngresosService } from '../service/caja-ingresos.service';

@Component({
  templateUrl: './caja-ingresos-delete-dialog.component.html',
})
export class CajaIngresosDeleteDialogComponent {
  cajaIngresos?: ICajaIngresos;

  constructor(protected cajaIngresosService: CajaIngresosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cajaIngresosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
