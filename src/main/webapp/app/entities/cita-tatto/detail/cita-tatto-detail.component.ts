import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { ICitaTatto } from '../cita-tatto.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';

@Component({
  selector: 'jhi-cita-tatto-detail',
  templateUrl: './cita-tatto-detail.component.html',
})
export class CitaTattoDetailComponent implements OnInit {
  citaTatto: ICitaTatto | null = null;

  constructor(
    protected dataUtils: DataUtils,
    protected activatedRoute: ActivatedRoute,
    protected storage: StateStorageService,
    protected router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ citaTatto }) => {
      this.citaTatto = citaTatto;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  parametroVerAbono(idCita: number): void {
    this.storage.pasoParametroCita(idCita);
    this.router.navigate(['/abono']);
  }

  parametroHacerAbono(idCita: number): void {
    this.storage.pasoParametroCita(idCita);
    this.router.navigate(['/abono/new']);
  }
}
