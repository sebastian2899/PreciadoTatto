import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAbono } from '../abono.model';

@Component({
  selector: 'jhi-abono-detail',
  templateUrl: './abono-detail.component.html',
})
export class AbonoDetailComponent implements OnInit {
  abono: IAbono | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ abono }) => {
      this.abono = abono;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
