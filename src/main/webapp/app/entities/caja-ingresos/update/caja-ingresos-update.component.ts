import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICajaIngresos, CajaIngresos } from '../caja-ingresos.model';
import { CajaIngresosService } from '../service/caja-ingresos.service';

@Component({
  selector: 'jhi-caja-ingresos-update',
  templateUrl: './caja-ingresos-update.component.html',
})
export class CajaIngresosUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    valorVendidoDia: [],
    valorRegistradoDia: [],
    diferencia: [],
  });

  constructor(protected cajaIngresosService: CajaIngresosService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cajaIngresos }) => {
      this.updateForm(cajaIngresos);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cajaIngresos = this.createFromForm();
    if (cajaIngresos.id !== undefined) {
      this.subscribeToSaveResponse(this.cajaIngresosService.update(cajaIngresos));
    } else {
      this.subscribeToSaveResponse(this.cajaIngresosService.create(cajaIngresos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICajaIngresos>>): void {
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

  protected updateForm(cajaIngresos: ICajaIngresos): void {
    this.editForm.patchValue({
      id: cajaIngresos.id,
      valorVendidoDia: cajaIngresos.valorVendidoDia,
      valorRegistradoDia: cajaIngresos.valorRegistradoDia,
      diferencia: cajaIngresos.diferencia,
    });
  }

  protected createFromForm(): ICajaIngresos {
    return {
      ...new CajaIngresos(),
      id: this.editForm.get(['id'])!.value,
      valorVendidoDia: this.editForm.get(['valorVendidoDia'])!.value,
      valorRegistradoDia: this.editForm.get(['valorRegistradoDia'])!.value,
      diferencia: this.editForm.get(['diferencia'])!.value,
    };
  }
}
