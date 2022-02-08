import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEgreso } from '../egreso.model';
import { EgresoService } from '../service/egreso.service';
import { EgresoDeleteDialogComponent } from '../delete/egreso-delete-dialog.component';

@Component({
  selector: 'jhi-egreso',
  templateUrl: './egreso.component.html',
})
export class EgresoComponent implements OnInit {
  egresos?: IEgreso[];
  isLoading = false;
  egresoDia?: number | null;

  constructor(protected egresoService: EgresoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.egresoService.queryDia().subscribe(
      (res: HttpResponse<IEgreso[]>) => {
        this.isLoading = false;
        this.egresos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  egresoDiario(): void {
    this.egresoService.egresosDia().subscribe(
      (res: HttpResponse<number>) => {
        this.egresoDia = res.body;
      },
      () => {
        this.egresoDia = 0;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
    this.egresoDiario();
  }

  trackId(index: number, item: IEgreso): number {
    return item.id!;
  }

  delete(egreso: IEgreso): void {
    const modalRef = this.modalService.open(EgresoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.egreso = egreso;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
