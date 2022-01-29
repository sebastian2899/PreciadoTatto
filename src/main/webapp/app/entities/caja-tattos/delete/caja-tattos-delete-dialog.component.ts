import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICajaTattos } from '../caja-tattos.model';
import { CajaTattosService } from '../service/caja-tattos.service';

@Component({
  templateUrl: './caja-tattos-delete-dialog.component.html',
})
export class CajaTattosDeleteDialogComponent {
  cajaTattos?: ICajaTattos;

  constructor(protected cajaTattosService: CajaTattosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cajaTattosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
