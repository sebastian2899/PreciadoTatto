import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVentas } from '../ventas.model';
import { VentasService } from '../service/ventas.service';
import { VentasDeleteDialogComponent } from '../delete/ventas-delete-dialog.component';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { ICliente } from 'app/entities/cliente/cliente.model';

@Component({
  selector: 'jhi-ventas',
  templateUrl: './ventas.component.html',
})
export class VentasComponent implements OnInit {
  ventas?: IVentas[];
  isLoading = false;
  nombre?: string | null;
  pA = 1;

  constructor(protected ventasService: VentasService, protected modalService: NgbModal, protected clienteService: ClienteService) {}

  loadAll(): void {
    this.isLoading = true;

    this.ventasService.query().subscribe(
      (res: HttpResponse<IVentas[]>) => {
        this.isLoading = false;
        this.ventas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IVentas): number {
    return item.id!;
  }

  delete(ventas: IVentas): void {
    const modalRef = this.modalService.open(VentasDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ventas = ventas;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
