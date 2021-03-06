import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEgreso, Egreso } from '../egreso.model';
import { EgresoService } from '../service/egreso.service';

@Component({
  selector: 'jhi-egreso-update',
  templateUrl: './egreso-update.component.html',
})
export class EgresoUpdateComponent implements OnInit {
  isSaving = false;
  titulo = 'Actualizar Egreso';

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    descripcion: [],
    valor: [],
  });

  constructor(protected egresoService: EgresoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ egreso }) => {
      if (egreso.id === undefined) {
        this.titulo = 'Crear Egreso';
        const today = dayjs().startOf('day');
        egreso.fechaCreacion = today;
      }

      this.updateForm(egreso);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const egreso = this.createFromForm();
    if (egreso.id !== undefined) {
      this.subscribeToSaveResponse(this.egresoService.update(egreso));
    } else {
      this.subscribeToSaveResponse(this.egresoService.create(egreso));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEgreso>>): void {
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

  protected updateForm(egreso: IEgreso): void {
    this.editForm.patchValue({
      id: egreso.id,
      fechaCreacion: egreso.fechaCreacion ? egreso.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      descripcion: egreso.descripcion,
      valor: egreso.valor,
    });
  }

  protected createFromForm(): IEgreso {
    return {
      ...new Egreso(),
      id: this.editForm.get(['id'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      descripcion: this.editForm.get(['descripcion'])!.value,
      valor: this.editForm.get(['valor'])!.value,
    };
  }
}
