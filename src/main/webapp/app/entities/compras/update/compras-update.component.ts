import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICompras, Compras } from '../compras.model';
import { ComprasService } from '../service/compras.service';

@Component({
  selector: 'jhi-compras-update',
  templateUrl: './compras-update.component.html',
})
export class ComprasUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    valorCompra: [],
    valorPagado: [],
    valorDeuda: [],
    estado: [],
  });

  constructor(protected comprasService: ComprasService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compras }) => {
      if (compras.id === undefined) {
        const today = dayjs().startOf('day');
        compras.fechaCreacion = today;
      }

      this.updateForm(compras);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compras = this.createFromForm();
    if (compras.id !== undefined) {
      this.subscribeToSaveResponse(this.comprasService.update(compras));
    } else {
      this.subscribeToSaveResponse(this.comprasService.create(compras));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompras>>): void {
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

  protected updateForm(compras: ICompras): void {
    this.editForm.patchValue({
      id: compras.id,
      fechaCreacion: compras.fechaCreacion ? compras.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      valorCompra: compras.valorCompra,
      valorPagado: compras.valorPagado,
      valorDeuda: compras.valorDeuda,
      estado: compras.estado,
    });
  }

  protected createFromForm(): ICompras {
    return {
      ...new Compras(),
      id: this.editForm.get(['id'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      valorCompra: this.editForm.get(['valorCompra'])!.value,
      valorPagado: this.editForm.get(['valorPagado'])!.value,
      valorDeuda: this.editForm.get(['valorDeuda'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
