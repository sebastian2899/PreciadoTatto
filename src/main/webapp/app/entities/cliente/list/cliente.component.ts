import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';
import { ClienteDeleteDialogComponent } from '../delete/cliente-delete-dialog.component';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-cliente',
  templateUrl: './cliente.component.html',
})
export class ClienteComponent implements OnInit {
  clientes?: ICliente[];
  isLoading = false;

  constructor(protected clienteService: ClienteService, protected modalService: NgbModal, protected route: Router) {}

  loadAll(): void {
    this.isLoading = true;

    this.clienteService.query().subscribe(
      (res: HttpResponse<ICliente[]>) => {
        this.isLoading = false;
        this.clientes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICliente): number {
    return item.id!;
  }

  agendarCita(): void {
    this.route.navigate(['/cita-tatto/new']);
  }

  delete(cliente: ICliente): void {
    const modalRef = this.modalService.open(ClienteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cliente = cliente;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
