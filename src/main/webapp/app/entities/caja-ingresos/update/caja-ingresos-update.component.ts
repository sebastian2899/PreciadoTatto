import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICajaIngresos, CajaIngresos } from '../caja-ingresos.model';
import { CajaIngresosService } from '../service/caja-ingresos.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-caja-ingresos-update',
  templateUrl: './caja-ingresos-update.component.html',
})
export class CajaIngresosUpdateComponent implements OnInit {
  @ViewChild('mensajeInicio', { static: true }) content: ElementRef | undefined;
  isSaving = false;
  titulo?: string | null;

  editForm = this.fb.group({
    id: [],
    valorVendidoDia: [],
    valorRegistradoDia: [],
    diferencia: [],
  });

  constructor(
    protected cajaIngresosService: CajaIngresosService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modal: NgbModal,
    protected alert: AlertService
  ) {}

  ngOnInit(): void {
    this.titulo = 'Actualizar Caja';
    this.activatedRoute.data.subscribe(({ cajaIngresos }) => {
      if (cajaIngresos.id === undefined) {
        this.titulo = 'Crear Caja';
      }
      this.updateForm(cajaIngresos);
    });
    this.consularCajaDia();
    this.modal.open(this.content);
  }

  previousState(): void {
    window.history.back();
  }

  consularCajaDia(): void {
    this.cajaIngresosService.cajaDia().subscribe((res: HttpResponse<number>) => {
      const valorDia = res.body;
      this.editForm.get(['valorVendidoDia'])?.setValue(valorDia);
      this.editForm.get(['diferencia'])?.setValue(valorDia);
      this.editForm.get(['valorRegistradoDia'])?.setValue(0);

      if (valorDia === 0 || valorDia === null) {
        this.editForm.get(['valorVendidoDia'])?.setValue(0);
      }
    });
  }

  calcularDiferencia(): void {
    const valorDia = this.editForm.get(['valorVendidoDia'])!.value;
    const valorRegistrado = this.editForm.get(['valorRegistradoDia'])!.value;

    if (valorRegistrado > valorDia) {
      this.alert.addAlert({
        type: 'danger',
        message: 'El valor a registrar no debe ser mayor al valor de los ingresos del dia.',
      });
      this.editForm.get(['valorRegistradoDia'])?.setValue(0);
      this.editForm.get(['diferencia'])?.setValue(valorDia);
    } else {
      const diferencia = valorDia - valorRegistrado;
      this.editForm.get(['diferencia'])?.setValue(diferencia);
    }
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
