import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEgreso } from '../egreso.model';
import { EgresoService } from '../service/egreso.service';
import { EgresoDeleteDialogComponent } from '../delete/egreso-delete-dialog.component';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-egreso',
  templateUrl: './egreso.component.html',
})
export class EgresoComponent implements OnInit {
  egresos?: IEgreso[];
  isLoading = false;
  egresoDia?: number | null;

  constructor(protected egresoService: EgresoService, protected modalService: NgbModal, protected alert: AlertService) {}

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

  loadAll2(): void {
    this.isLoading = true;

    this.egresoService.query().subscribe(
      (res: HttpResponse<IEgreso[]>) => {
        this.isLoading = false;
        this.egresos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
    this.egresoDiario();
  }

  generarReporteEgreso(): void {
    this.egresoService.reportEgreso().subscribe(
      (res: any) => {
        const file = new Blob([res], { type: 'application/pdf' });
        const url = URL.createObjectURL(file);
        window.open(url);
      },
      () => {
        this.alert.addAlert({
          type: 'warning',
          message: 'Error al generar el archivo pdf',
        });
      }
    );
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
