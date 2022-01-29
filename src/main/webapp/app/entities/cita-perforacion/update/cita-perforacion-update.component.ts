import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICitaPerforacion, CitaPerforacion } from '../cita-perforacion.model';
import { CitaPerforacionService } from '../service/cita-perforacion.service';

@Component({
  selector: 'jhi-cita-perforacion-update',
  templateUrl: './cita-perforacion-update.component.html',
})
export class CitaPerforacionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    fechaCita: [],
    hora: [],
    nombreCliente: [],
    valorPerforacion: [],
    valorPagado: [],
    valorDeuda: [],
    estado: [],
  });

  constructor(
    protected citaPerforacionService: CitaPerforacionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ citaPerforacion }) => {
      if (citaPerforacion.id === undefined) {
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

  save(): void {
    this.isSaving = true;
    const citaPerforacion = this.createFromForm();
    if (citaPerforacion.id !== undefined) {
      this.subscribeToSaveResponse(this.citaPerforacionService.update(citaPerforacion));
    } else {
      this.subscribeToSaveResponse(this.citaPerforacionService.create(citaPerforacion));
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
      valorPerforacion: citaPerforacion.valorPerforacion,
      valorPagado: citaPerforacion.valorPagado,
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
      valorPerforacion: this.editForm.get(['valorPerforacion'])!.value,
      valorPagado: this.editForm.get(['valorPagado'])!.value,
      valorDeuda: this.editForm.get(['valorDeuda'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
