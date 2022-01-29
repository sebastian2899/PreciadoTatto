import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICitaPerforacion } from '../cita-perforacion.model';
import { CitaPerforacionService } from '../service/cita-perforacion.service';
import { CitaPerforacionDeleteDialogComponent } from '../delete/cita-perforacion-delete-dialog.component';

@Component({
  selector: 'jhi-cita-perforacion',
  templateUrl: './cita-perforacion.component.html',
})
export class CitaPerforacionComponent implements OnInit {
  citaPerforacions?: ICitaPerforacion[];
  isLoading = false;

  constructor(protected citaPerforacionService: CitaPerforacionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.citaPerforacionService.query().subscribe(
      (res: HttpResponse<ICitaPerforacion[]>) => {
        this.isLoading = false;
        this.citaPerforacions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICitaPerforacion): number {
    return item.id!;
  }

  delete(citaPerforacion: ICitaPerforacion): void {
    const modalRef = this.modalService.open(CitaPerforacionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.citaPerforacion = citaPerforacion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
