import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICitaPerforacion } from '../cita-perforacion.model';
import { CitaPerforacionService } from '../service/cita-perforacion.service';

@Component({
  templateUrl: './cita-perforacion-delete-dialog.component.html',
})
export class CitaPerforacionDeleteDialogComponent {
  citaPerforacion?: ICitaPerforacion;

  constructor(protected citaPerforacionService: CitaPerforacionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.citaPerforacionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
