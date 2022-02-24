import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { CitaTatto, ICitaTatto } from '../cita-tatto.model';
import { CitaTattoService } from '../service/cita-tatto.service';
import { CitaTattoDeleteDialogComponent } from '../delete/cita-tatto-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { Router } from '@angular/router';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-cita-tatto',
  templateUrl: './cita-tatto.component.html',
})
export class CitaTattoComponent implements OnInit {
  citaTattos?: ICitaTatto[];
  isLoading = false;
  pA = 1;
  nombreCliente = '';
  hora = '';
  citaTatto?: ICitaTatto | null;
  estadoCita = [''];

  constructor(
    private storage: StateStorageService,
    protected router: Router,
    protected citaTattoService: CitaTattoService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    protected alert: AlertService
  ) {}

  loadAll(): void {
    this.citaTatto = new CitaTatto();

    this.citaTattoService.citasPorFiltro(this.citaTatto).subscribe(
      (res: HttpResponse<ICitaTatto[]>) => {
        this.isLoading = false;
        this.citaTattos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  consultarCitaFiltro(): void {
    this.isLoading = true;
    this.citaTatto = new CitaTatto();
    this.citaTatto.infoCliente = this.nombreCliente;
    this.citaTatto.hora = this.hora;

    this.citaTattoService.citasPorFiltro(this.citaTatto).subscribe(
      (res: HttpResponse<ICitaTatto[]>) => {
        this.isLoading = false;
        this.citaTattos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  pasoParametroCita(idCita: number): void {
    this.storage.pasoParametroCita(idCita);
    this.router.navigate(['abono/new']);
  }

  pasoParametroVerAbono(idCita: number): void {
    this.storage.pasoParametroCita(idCita);
    this.router.navigate(['abono']);
  }

  trackId(index: number, item: ICitaTatto): number {
    return item.id!;
  }

  generarReporte(): void {
    this.citaTattoService.generarReporte().subscribe(
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

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(citaTatto: ICitaTatto): void {
    const modalRef = this.modalService.open(CitaTattoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.citaTatto = citaTatto;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
