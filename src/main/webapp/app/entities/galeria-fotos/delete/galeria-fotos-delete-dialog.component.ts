import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGaleriaFotos } from '../galeria-fotos.model';
import { GaleriaFotosService } from '../service/galeria-fotos.service';

@Component({
  templateUrl: './galeria-fotos-delete-dialog.component.html',
})
export class GaleriaFotosDeleteDialogComponent {
  galeriaFotos?: IGaleriaFotos;

  constructor(protected galeriaFotosService: GaleriaFotosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.galeriaFotosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
