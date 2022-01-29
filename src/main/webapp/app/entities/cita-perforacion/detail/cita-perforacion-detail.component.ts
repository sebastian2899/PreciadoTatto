import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICitaPerforacion } from '../cita-perforacion.model';

@Component({
  selector: 'jhi-cita-perforacion-detail',
  templateUrl: './cita-perforacion-detail.component.html',
})
export class CitaPerforacionDetailComponent implements OnInit {
  citaPerforacion: ICitaPerforacion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ citaPerforacion }) => {
      this.citaPerforacion = citaPerforacion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
