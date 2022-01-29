import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICajaIngresos } from '../caja-ingresos.model';

@Component({
  selector: 'jhi-caja-ingresos-detail',
  templateUrl: './caja-ingresos-detail.component.html',
})
export class CajaIngresosDetailComponent implements OnInit {
  cajaIngresos: ICajaIngresos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cajaIngresos }) => {
      this.cajaIngresos = cajaIngresos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
