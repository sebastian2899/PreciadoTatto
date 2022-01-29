import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVentas } from '../ventas.model';
import { VentasService } from '../service/ventas.service';

@Component({
  templateUrl: './ventas-delete-dialog.component.html',
})
export class VentasDeleteDialogComponent {
  ventas?: IVentas;

  constructor(protected ventasService: VentasService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ventasService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
