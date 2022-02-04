import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAbono } from '../abono.model';
import { AbonoService } from '../service/abono.service';

@Component({
  templateUrl: './abono-delete-dialog.component.html',
})
export class AbonoDeleteDialogComponent {
  abono?: IAbono;

  constructor(protected abonoService: AbonoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.abonoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
