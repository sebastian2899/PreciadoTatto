import { HttpResponse } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import * as dayjs from 'dayjs';
import { EgresoService } from '../service/egreso.service';

@Component({
  selector: 'jhi-egreso-mes',
  templateUrl: './egreso-mes.component.html',
  styleUrls: ['./egreso-mes.component.scss'],
})
export class EgresoMesComponent implements OnInit {
  @ViewChild('mensajeInicio', { static: true }) content: ElementRef | undefined;

  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  isSaving = false;
  egresoTotal?: number | null;

  constructor(protected egresoService: EgresoService, protected modal: NgbModal) {}

  ngOnInit(): void {
    this.fechaInicio = dayjs().startOf('day');
    this.fechaFin = dayjs().startOf('day');
    this.modal.open(this.content);
  }

  consultarEgresoMes(): void {
    if (this.fechaInicio && this.fechaFin) {
      this.egresoService.agresoMes(this.fechaInicio.toString(), this.fechaFin.toString()).subscribe(
        (res: HttpResponse<number>) => {
          this.egresoTotal = res.body;
        },
        () => {
          this.egresoTotal = 0;
        }
      );
    }
  }

  previusState(): void {
    window.history.back();
  }
}
