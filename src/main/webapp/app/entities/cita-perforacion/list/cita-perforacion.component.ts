import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { CitaPerforacion, ICitaPerforacion } from '../cita-perforacion.model';
import { CitaPerforacionService } from '../service/cita-perforacion.service';
import { CitaPerforacionDeleteDialogComponent } from '../delete/cita-perforacion-delete-dialog.component';
import { AlertService } from 'app/core/util/alert.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { Router } from '@angular/router';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-cita-perforacion',
  templateUrl: './cita-perforacion.component.html',
})
export class CitaPerforacionComponent implements OnInit {
  citaPerforacions?: ICitaPerforacion[];
  citaPerforacion?: ICitaPerforacion;
  isLoading = false;
  fechaCita?: dayjs.Dayjs | null;
  nombreCliente = '';
  hora = '';
  pA = 1;

  constructor(
    protected citaPerforacionService: CitaPerforacionService,
    protected modalService: NgbModal,
    protected alert: AlertService,
    protected sotagre: StateStorageService,
    protected route: Router
  ) {}

  loadAll(): void {
    this.isLoading = false;
    this.citaPerforacion = new CitaPerforacion();

    this.citaPerforacionService.cirasPorFiltro(this.citaPerforacion).subscribe(
      (res: HttpResponse<ICitaPerforacion[]>) => {
        this.isLoading = false;
        this.citaPerforacions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  consultarCitaPorFiltro(): void {
    this.citaPerforacion = new CitaPerforacion();
    this.citaPerforacion.nombreCliente = this.nombreCliente;
    this.citaPerforacion.hora = this.hora;
    this.citaPerforacionService.cirasPorFiltro(this.citaPerforacion).subscribe(
      (res: HttpResponse<ICitaPerforacion[]>) => {
        this.isLoading = false;
        this.citaPerforacions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  consultarCitasPorFecha(): void {
    if (this.fechaCita) {
      this.citaPerforacionService.citasPorFecha(this.fechaCita.toString()).subscribe(
        (res: HttpResponse<ICitaPerforacion[]>) => {
          this.citaPerforacions = res.body ?? [];
        },
        () => {
          this.citaPerforacions = [];
        }
      );
    }
  }

  generarReporteMensual(): void {
    this.citaPerforacionService.generarReporte().subscribe(
      (res: any) => {
        const file = new Blob([res], { type: 'application/pdf' });
        const fileURL = URL.createObjectURL(file);
        window.open(fileURL);
      },
      () => {
        this.alert.addAlert({
          type: 'warning',
          message: 'Se presento un error en la previsualización',
        });
      }
    );
  }

  realizarAbono(idCita: number): void {
    this.sotagre.pasoParametroCita(idCita);
    this.route.navigate(['abono/new']);
  }

  pasoParametroVerAbonos(idCita: number): void {
    this.sotagre.pasoParametroCita(idCita);
    this.route.navigate(['abono']);
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICitaPerforacion): number {
    return item.id!;
  }

  delete(citaPerforacion: ICitaPerforacion): void {
    const modalRef = this.modalService.open(CitaPerforacionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.citaPerforacion = citaPerforacion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
