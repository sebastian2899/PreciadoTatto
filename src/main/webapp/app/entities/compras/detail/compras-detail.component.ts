import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompras } from '../compras.model';

@Component({
  selector: 'jhi-compras-detail',
  templateUrl: './compras-detail.component.html',
})
export class ComprasDetailComponent implements OnInit {
  compras: ICompras | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compras }) => {
      this.compras = compras;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
