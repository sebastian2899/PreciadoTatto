import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICitaPerforacion, CitaPerforacion } from '../cita-perforacion.model';
import { CitaPerforacionService } from '../service/cita-perforacion.service';
import { AlertService } from 'app/core/util/alert.service';
import { IMensajeValidacionCita } from 'app/entities/cita-tatto/mensaje-validacion';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-cita-perforacion-update',
  templateUrl: './cita-perforacion-update.component.html',
})
export class CitaPerforacionUpdateComponent implements OnInit {
  @ViewChild('mensajeValidacion', { static: true }) content: ElementRef | undefined;

  isSaving = false;
  titulo = 'Actualizar cita perforacion';
  tipoDeCitas = ['Cita Perforacion', 'Cita Tattoo'];
  valorMafe?: number | null;
  valorCaja?: number | null;
  mensajeValidacion?: IMensajeValidacionCita | null;
  mensaje?: string | null;

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    fechaCita: [],
    hora: [],
    nombreCliente: [],
    valorTotalDescueto: [],
    tipoCita: [],
    valorPerforacion: [],
    valorTotalDescuento: [],
    valorCaja: [],
    valorPagado: [],
    valorDeuda: [],
    estado: [],
  });

  constructor(
    protected citaPerforacionService: CitaPerforacionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private alert: AlertService,
    protected modal: NgbModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ citaPerforacion }) => {
      if (citaPerforacion.id === undefined) {
        this.titulo = 'Agendar cita perforacion';
        const today = dayjs().startOf('day');
        citaPerforacion.fechaCreacion = today;
        citaPerforacion.fechaCita = today;
      }

      this.updateForm(citaPerforacion);
    });
  }

  previousState(): void {
    window.history.back();
  }

  calcularDescuentos(): void {
    const valorPagado = this.editForm.get(['valorPagado'])!.value;
    const tipoCita = this.editForm.get(['tipoCita'])!.value;
    if (valorPagado) {
      if (tipoCita === 'Cita Perforacion') {
        const DESCUENTO = 50;
        const VALOR_DESCONTAR = (valorPagado * DESCUENTO) / 100;
        this.valorCaja = VALOR_DESCONTAR;
        this.valorMafe = valorPagado - VALOR_DESCONTAR;
        this.editForm.get(['valorCaja'])?.setValue(this.valorCaja);
        this.editForm.get(['valorTotalDescuento'])?.setValue(this.valorMafe);
      } else if (tipoCita === 'Cita Tattoo') {
        const DESCUENTO = 30;
        const VALOR_DESCONTAR = (valorPagado * DESCUENTO) / 100;
        this.valorCaja = VALOR_DESCONTAR;
        this.valorMafe = valorPagado - VALOR_DESCONTAR;
        this.editForm.get(['valorCaja'])?.setValue(this.valorCaja);
        this.editForm.get(['valorTotalDescuento'])?.setValue(this.valorMafe);
      }
    } else if (valorPagado === 0 || valorPagado === null) {
      this.valorCaja = 0;
      this.valorMafe = 0;
    }
  }

  validarFechaSave(): void {
    this.isSaving = true;
    const citaPerforacio = this.createFromForm();
    this.citaPerforacionService.validarCitaSave(citaPerforacio).subscribe((res: HttpResponse<IMensajeValidacionCita>) => {
      this.mensajeValidacion = res.body;
      if (this.mensajeValidacion?.mensaje) {
        this.mensaje = this.mensajeValidacion.mensaje;
        this.modal.open(this.content);
        this.isSaving = false;
      } else {
        this.save();
      }
    });
  }

  save(): void {
    this.isSaving = true;
    const citaPerforacion = this.createFromForm();
    if (citaPerforacion.id !== undefined) {
      this.subscribeToSaveResponse(this.citaPerforacionService.update(citaPerforacion));
    } else {
      this.subscribeToSaveResponse(this.citaPerforacionService.create(citaPerforacion));
    }
  }

  calcularValores(): void {
    const valorPerfo = this.editForm.get(['valorPerforacion'])!.value;
    const valorPagado = this.editForm.get(['valorPagado'])!.value;

    this.editForm.get(['valorDeuda'])?.setValue(valorPerfo);
    this.editForm.get(['estado'])?.setValue('Deuda');

    if (valorPagado) {
      if (valorPagado > valorPerfo) {
        this.alert.addAlert({
          type: 'danger',
          message: 'El valor a pagar en el sistema no debe ser mayor al valor de la perforacion.',
        });
        this.editForm.get(['valorPagado'])?.setValue(0);
        this.editForm.get(['valorDeuda'])?.setValue(valorPerfo);
      }

      const deuda = valorPerfo - valorPagado;
      this.editForm.get(['valorDeuda'])?.setValue(deuda);

      let estado = null;
      deuda === 0 ? (estado = 'Pagado') : (estado = 'Deuda');

      this.editForm.get(['estado'])?.setValue(estado);
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICitaPerforacion>>): void {
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

  protected updateForm(citaPerforacion: ICitaPerforacion): void {
    this.editForm.patchValue({
      id: citaPerforacion.id,
      fechaCreacion: citaPerforacion.fechaCreacion ? citaPerforacion.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      fechaCita: citaPerforacion.fechaCita ? citaPerforacion.fechaCita.format(DATE_TIME_FORMAT) : null,
      hora: citaPerforacion.hora,
      nombreCliente: citaPerforacion.nombreCliente,
      tipCita: citaPerforacion.tipoCita,
      valorPerforacion: citaPerforacion.valorPerforacion,
      valorPagado: citaPerforacion.valorPagado,
      valorTotalDescuento: citaPerforacion.valorTotalDescuento,
      valorCaja: citaPerforacion.valorCaja,
      valorDeuda: citaPerforacion.valorDeuda,
      estado: citaPerforacion.estado,
    });
  }

  protected createFromForm(): ICitaPerforacion {
    return {
      ...new CitaPerforacion(),
      id: this.editForm.get(['id'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaCita: this.editForm.get(['fechaCita'])!.value ? dayjs(this.editForm.get(['fechaCita'])!.value, DATE_TIME_FORMAT) : undefined,
      hora: this.editForm.get(['hora'])!.value,
      nombreCliente: this.editForm.get(['nombreCliente'])!.value,
      tipoCita: this.editForm.get(['tipoCita'])!.value,
      valorTotalDescuento: this.editForm.get(['valorTotalDescuento'])!.value,
      valorCaja: this.editForm.get(['valorCaja'])!.value,
      valorPerforacion: this.editForm.get(['valorPerforacion'])!.value,
      valorPagado: this.editForm.get(['valorPagado'])!.value,
      valorDeuda: this.editForm.get(['valorDeuda'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
