import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICajaIngresos } from '../caja-ingresos.model';
import { CajaIngresosService } from '../service/caja-ingresos.service';
import { CajaIngresosDeleteDialogComponent } from '../delete/caja-ingresos-delete-dialog.component';

@Component({
  selector: 'jhi-caja-ingresos',
  templateUrl: './caja-ingresos.component.html',
})
export class CajaIngresosComponent implements OnInit {
  cajaIngresos?: ICajaIngresos[];
  isLoading = false;

  constructor(protected cajaIngresosService: CajaIngresosService, protected modalService: NgbModal) {}

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
