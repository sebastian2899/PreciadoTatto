import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAbono, Abono } from '../abono.model';
import { AbonoService } from '../service/abono.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { AlertService } from 'app/core/util/alert.service';
import { CitaPerforacionService } from 'app/entities/cita-perforacion/service/cita-perforacion.service';
import { ICitaPerforacion } from 'app/entities/cita-perforacion/cita-perforacion.model';

@Component({
  selector: 'jhi-abono-update',
  templateUrl: './abono-update.component.html',
})
export class AbonoUpdateComponent implements OnInit {
  isSaving = false;
  idCita?: number | null;
  desBoton?: boolean | null;
  consultarCitaPerf?: boolean | null;
  citaPerfo?: ICitaPerforacion | null;
  resp?: number | null;
  valorCaja = 0;
  valorMafe = 0;

  editForm = this.fb.group({
    id: [],
    fechaAbono: [],
    idCita: [],
    valorAbono: [],
    deuda: [],
  });

  constructor(
    private alert: AlertService,
    private storage: StateStorageService,
    protected abonoService: AbonoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected citaPerforacionService: CitaPerforacionService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ citaTatto }) => {
      this.idCita = this.storage.getParametroCita();
      const today = dayjs().startOf('day');
      const fecha = dayjs(today, DATE_TIME_FORMAT).format(DATE_TIME_FORMAT);
      this.editForm.get(['fechaAbono'])?.setValue(fecha);
      this.editForm.get(['idCita'])?.setValue(this.idCita);

      if (this.idCita) {
        this.consultarValorDeuda(this.idCita);
        this.consultarAbonoPerforacion(this.idCita);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  consultarAbonoPerforacion(id: number): void {
    this.citaPerforacionService.consulTipoCita(Number(id)).subscribe((res: HttpResponse<number>) => {
      this.resp = res.body;
      if (Number(this.resp) === Number(1)) {
        this.consultarCitaPerf = true;
        this.alert.addAlert({
          type: 'success',
          message: 'Abono destinado a Cita:  (Maria Fernanda)',
        });
      } else if (Number(this.resp) === Number(2)) {
        this.consultarCitaPerf = false;
        this.alert.addAlert({
          type: 'success',
          message: 'Abono destinado a Cita (Steven Preciado)',
        });
      }
    });
  }

  calcularDescuentos(): void {
    if (this.consultarCitaPerf && this.resp === 1) {
      this.citaPerforacionService.find(this.idCita!).subscribe((res: HttpResponse<ICitaPerforacion>) => {
        this.citaPerfo = res.body;

        const valorAbono = this.editForm.get(['valorAbono'])!.value;
        if (this.citaPerfo?.tipoCita === 'Cita Tattoo') {
          const DESCUENTO = 30;
          this.valorCaja = (valorAbono * DESCUENTO) / 100;
          this.valorMafe = valorAbono - this.valorCaja;
        } else if (this.citaPerfo?.tipoCita === 'Cita Perforacion') {
          const DESCUENTO = 50;
          this.valorCaja = (valorAbono * DESCUENTO) / 100;
          this.valorMafe = valorAbono - this.valorCaja;
        }
        if (valorAbono === 0 || valorAbono === null) {
          this.valorCaja = 0;
          this.valorMafe = 0;
        }
      });
    }
  }

  consultarValorDeuda(idCita: number): void {
    this.abonoService.valorDeuda(idCita).subscribe((res: HttpResponse<number>) => {
      this.editForm.get(['deuda'])?.setValue(res.body);
    });
  }

  restarAbono(): void {
    const valorAbonar = this.editForm.get(['valorAbono'])!.value;
    const deuda = this.editForm.get(['deuda'])!.value;
    if (valorAbonar > deuda) {
      this.alert.addAlert({
        type: 'danger',
        message: 'El valor a abonar no debe ser mayor al valor de la deuda de la cita.',
      });
      this.editForm.get(['valorAbono'])?.setValue(null);
      this.desBoton = true;
    } else {
      this.desBoton = false;
    }
  }

  save(): void {
    this.isSaving = true;
    const abono = this.createFromForm();
    this.subscribeToSaveResponse(this.abonoService.create(abono));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAbono>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(abono: IAbono): void {
    this.editForm.patchValue({
      id: abono.id,
      fechaAbono: abono.fechaAbono ? abono.fechaAbono.format(DATE_TIME_FORMAT) : null,
      idCita: abono.idCita,
      valorAbono: abono.valorAbono,
    });
  }

  protected createFromForm(): IAbono {
    return {
      ...new Abono(),
      id: undefined,
      fechaAbono: this.editForm.get(['fechaAbono'])!.value ? dayjs(this.editForm.get(['fechaAbono'])!.value, DATE_TIME_FORMAT) : undefined,
      idCita: this.idCita,
      valorAbono: this.editForm.get(['valorAbono'])!.value,
    };
  }
}
