import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

import { IVentas } from '../ventas.model';

@Component({
  selector: 'jhi-ventas-detail',
  templateUrl: './ventas-detail.component.html',
})
export class VentasDetailComponent implements OnInit {
  ventas: IVentas | null = null;
  clientes?: ICliente | null;
  nombre?: string | null;

  constructor(protected activatedRoute: ActivatedRoute, protected clienteService: ClienteService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ventas }) => {
      if (ventas !== undefined) {
        this.clienteService.find(ventas.idCliente).subscribe((res: HttpResponse<ICliente>) => {
          this.clientes = res.body;
          this.nombre = this.clientes?.nombre;
        });
      }
      this.ventas = ventas;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
