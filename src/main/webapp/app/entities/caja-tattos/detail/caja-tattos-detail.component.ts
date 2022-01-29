import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICajaTattos } from '../caja-tattos.model';

@Component({
  selector: 'jhi-caja-tattos-detail',
  templateUrl: './caja-tattos-detail.component.html',
})
export class CajaTattosDetailComponent implements OnInit {
  cajaTattos: ICajaTattos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cajaTattos }) => {
      this.cajaTattos = cajaTattos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
