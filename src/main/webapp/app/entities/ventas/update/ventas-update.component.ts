import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IVentas, Ventas } from '../ventas.model';
import { VentasService } from '../service/ventas.service';

@Component({
  selector: 'jhi-ventas-update',
  templateUrl: './ventas-update.component.html',
})
export class VentasUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    idCliente: [],
    valorVenta: [],
    valorPagado: [],
    valorDeuda: [],
    estado: [],
  });

  constructor(protected ventasService: VentasService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ventas }) => {
      this.updateForm(ventas);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ventas = this.createFromForm();
    if (ventas.id !== undefined) {
      this.subscribeToSaveResponse(this.ventasService.update(ventas));
    } else {
      this.subscribeToSaveResponse(this.ventasService.create(ventas));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVentas>>): void {
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

  protected updateForm(ventas: IVentas): void {
    this.editForm.patchValue({
      id: ventas.id,
      fechaCreacion: ventas.fechaCreacion,
      idCliente: ventas.idCliente,
      valorVenta: ventas.valorVenta,
      valorPagado: ventas.valorPagado,
      valorDeuda: ventas.valorDeuda,
      estado: ventas.estado,
    });
  }

  protected createFromForm(): IVentas {
    return {
      ...new Ventas(),
      id: this.editForm.get(['id'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value,
      idCliente: this.editForm.get(['idCliente'])!.value,
      valorVenta: this.editForm.get(['valorVenta'])!.value,
      valorPagado: this.editForm.get(['valorPagado'])!.value,
      valorDeuda: this.editForm.get(['valorDeuda'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
