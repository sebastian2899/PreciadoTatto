import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import * as dayjs from 'dayjs';
import { ICajaFechaIngresos } from '../caja-fechas.';
import { CajaIngresosService } from '../service/caja-ingresos.service';

@Component({
  selector: 'jhi-caja-ing-fecha',
  templateUrl: './caja-ing-fecha.component.html',
  styleUrls: ['./caja-ing-fecha.component.scss'],
})
export class CajaIngFechaComponent implements OnInit {
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  valoresIngresos?: ICajaFechaIngresos | null;
  isSaving = false;

  constructor(protected cajaIngresoService: CajaIngresosService, protected modal: NgbModal) {}

  ngOnInit(): void {
    this.fechaInicio = dayjs().startOf('day');
    this.fechaFin = dayjs().startOf('day');
  }

  previusState(): void {
    window.history.back();
  }

  consultarCajaFechas(): void {
    this.isSaving = true;
    if (this.fechaInicio && this.fechaFin) {
      this.cajaIngresoService.cajaIngFechas(this.fechaInicio.toString(), this.fechaFin.toString()).subscribe(
        (res: HttpResponse<ICajaFechaIngresos>) => {
          this.valoresIngresos = res.body;
          this.isSaving = false;
        },
        () => {
          this.valoresIngresos = null;
          this.isSaving = false;
        }
      );
    }
  }
}
