import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICitaTatto } from '../cita-tatto.model';
import { CitaTattoService } from '../service/cita-tatto.service';

@Component({
  templateUrl: './cita-tatto-delete-dialog.component.html',
})
export class CitaTattoDeleteDialogComponent {
  citaTatto?: ICitaTatto;

  constructor(protected citaTattoService: CitaTattoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.citaTattoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
