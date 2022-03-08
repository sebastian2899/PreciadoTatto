import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICajaIngresos } from '../caja-ingresos.model';
import { CajaIngresosService } from '../service/caja-ingresos.service';
import { CajaIngresosDeleteDialogComponent } from '../delete/caja-ingresos-delete-dialog.component';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-caja-ingresos',
  templateUrl: './caja-ingresos.component.html',
})
export class CajaIngresosComponent implements OnInit {
  cajaIngresos?: ICajaIngresos[];
  isLoading = false;
  pA = 1;

  constructor(protected cajaIngresosService: CajaIngresosService, protected modalService: NgbModal, protected alert: AlertService) {}

  loadAll(): void {
    this.isLoading = true;

    this.cajaIngresosService.query().subscribe(
      (res: HttpResponse<ICajaIngresos[]>) => {
        this.isLoading = false;
        this.cajaIngresos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  generarReporteIngreso(): void {
    this.cajaIngresosService.generarReport().subscribe(
      (res: any) => {
        const FILE = new Blob([res], { type: 'application/pdf' });
        const url = URL.createObjectURL(FILE);
        window.open(url);
      },
      () => {
        this.alert.addAlert({
          type: 'warning',
          message: 'Error al generar el archivo PDF',
        });
      }
    );
  }

  trackId(index: number, item: ICajaIngresos): number {
    return item.id!;
  }

  delete(cajaIngresos: ICajaIngresos): void {
    const modalRef = this.modalService.open(CajaIngresosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cajaIngresos = cajaIngresos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
