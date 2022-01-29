import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVentas } from '../ventas.model';

@Component({
  selector: 'jhi-ventas-detail',
  templateUrl: './ventas-detail.component.html',
})
export class VentasDetailComponent implements OnInit {
  ventas: IVentas | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ventas }) => {
      this.ventas = ventas;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
