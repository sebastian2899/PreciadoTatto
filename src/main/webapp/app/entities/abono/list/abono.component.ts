import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAbono } from '../abono.model';
import { AbonoService } from '../service/abono.service';
import { AbonoDeleteDialogComponent } from '../delete/abono-delete-dialog.component';
import { StateStorageService } from 'app/core/auth/state-storage.service';

@Component({
  selector: 'jhi-abono',
  templateUrl: './abono.component.html',
})
export class AbonoComponent implements OnInit {
  @ViewChild('mensajeInicio', { static: true }) content: ElementRef | undefined;

  abonos?: IAbono[];
  isLoading = false;
  idCita?: number | null;

  constructor(private storage: StateStorageService, protected abonoService: AbonoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;
    this.idCita = this.storage.getParametroCita();

    this.abonoService.queryPorCita(this.idCita!).subscribe(
      (res: HttpResponse<IAbono[]>) => {
        this.isLoading = false;
        this.abonos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
        this.abonos = [];
      }
    );

    this.modalService.open(this.content);
  }

  ngOnInit(): void {
    this.loadAll();
  }

  regresar(): void {
    window.history.back();
  }

  trackId(index: number, item: IAbono): number {
    return item.id!;
  }

  delete(abono: IAbono): void {
    const modalRef = this.modalService.open(AbonoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.abono = abono;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
