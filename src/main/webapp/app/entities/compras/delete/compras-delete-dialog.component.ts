import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompras } from '../compras.model';
import { ComprasService } from '../service/compras.service';

@Component({
  templateUrl: './compras-delete-dialog.component.html',
})
export class ComprasDeleteDialogComponent {
  compras?: ICompras;

  constructor(protected comprasService: ComprasService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.comprasService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
