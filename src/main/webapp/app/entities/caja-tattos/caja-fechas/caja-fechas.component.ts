import { HttpResponse } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import * as dayjs from 'dayjs';
import { ICajaFechasTattoos } from '../caja-fechas.model';
import { CajaTattosService } from '../service/caja-tattos.service';

@Component({
  selector: 'jhi-caja-fechas',
  templateUrl: './caja-fechas.component.html',
  styleUrls: ['./caja-fechas.component.scss'],
})
export class CajaFechasComponent implements OnInit {
  @ViewChild('mensajeInicio', { static: true }) content: ElementRef | undefined;

  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  isSaving = false;
  cajaFechas?: ICajaFechasTattoos | null;

  constructor(protected cajaService: CajaTattosService, protected modal: NgbModal) {
    this.fechaInicio = dayjs().startOf('day');
    this.fechaInicio = dayjs().startOf('day');
  }

  ngOnInit(): void {
    this.fechaInicio = dayjs().startOf('day');
    this.fechaInicio = dayjs().startOf('day');

    this.modal.open(this.content);
  }

  consultarCajaFechas(): void {
    this.isSaving = true;
    if (this.fechaInicio && this.fechaFin) {
      this.cajaService.cajaPorFecha(this.fechaInicio.toString(), this.fechaFin.toString()).subscribe(
        (res: HttpResponse<ICajaFechasTattoos>) => {
          this.cajaFechas = res.body;
          this.isSaving = false;
        },
        () => {
          this.cajaFechas = null;
          this.isSaving = false;
        }
      );
    }
  }

  previusState(): void {
    window.history.back();
  }
}
