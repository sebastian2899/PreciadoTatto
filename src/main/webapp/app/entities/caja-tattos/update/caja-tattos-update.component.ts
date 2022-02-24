import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICajaTattos, CajaTattos } from '../caja-tattos.model';
import { CajaTattosService } from '../service/caja-tattos.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { AlertService } from 'app/core/util/alert.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NullTemplateVisitor } from '@angular/compiler';

@Component({
  selector: 'jhi-caja-tattos-update',
  templateUrl: './caja-tattos-update.component.html',
})
export class CajaTattosUpdateComponent implements OnInit {
  isSaving = false;
  saving?: boolean | undefined;
  validarCaja?: boolean | undefined;
  titulo = 'Actualizar caja tattoos';

  @ViewChild('mensajeInicio', { static: true }) content: ElementRef | undefined;

  editForm = this.fb.group({
    id: [],
    valorTattoDia: [],
    valorRegistrado: [],
    diferencia: [],
  });

  constructor(
    protected cajaTattosService: CajaTattosService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected alertService: AlertService,
    protected modal: NgbModal
  ) {}
  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cajaTattos }) => {
      if (cajaTattos.id === undefined) {
        this.titulo = 'Crear caja tattoo';
      }
      this.updateForm(cajaTattos);
    });
    this.valorDia();

    this.modal.open(this.content);
  }

  valorDia(): void {
    this.cajaTattosService.valorDia().subscribe((res: HttpResponse<number>) => {
      const valorDia = res.body;
      this.editForm.get(['valorTattoDia'])?.setValue(valorDia);
      this.editForm.get(['diferencia'])?.setValue(valorDia);

      valorDia === 0 ? (this.saving = true) : (this.saving = false);
    }),
      () => {
        const valorCaja = 0;
        this.editForm.get(['valorfinaldia'])!.setValue(valorCaja);
      };
  }

  calcularDiferencia(): void {
    const valorDia = this.editForm.get(['valorTattoDia'])!.value;
    const valorRegistrado = this.editForm.get(['valorRegistrado'])!.value;

    if (valorRegistrado === 0 || valorRegistrado === undefined || valorRegistrado === null) {
      this.editForm.get(['diferencia'])?.setValue(valorDia);
    }

    if (valorRegistrado > valorDia) {
      this.alertService.addAlert({
        type: 'danger',
        message: 'El valor a registrar no debe ni deberia ser mayor al valor registrado durante el dia',
      });
      this.editForm.get(['valorRegistrado'])?.setValue(null);
      this.editForm.get(['diferencia'])?.setValue(valorDia);
    } else {
      const diferencia = valorDia - valorRegistrado;
      this.editForm.get(['diferencia'])?.setValue(diferencia);
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cajaTattos = this.createFromForm();
    if (cajaTattos.id !== undefined) {
      this.subscribeToSaveResponse(this.cajaTattosService.update(cajaTattos));
    } else {
      this.subscribeToSaveResponse(this.cajaTattosService.create(cajaTattos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICajaTattos>>): void {
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

  protected updateForm(cajaTattos: ICajaTattos): void {
    this.editForm.patchValue({
      id: cajaTattos.id,
      valorTattoDia: cajaTattos.valorTattoDia,
      valorRegistrado: cajaTattos.valorRegistrado,
      diferencia: cajaTattos.diferencia,
    });
  }

  protected createFromForm(): ICajaTattos {
    return {
      ...new CajaTattos(),
      id: this.editForm.get(['id'])!.value,
      valorTattoDia: this.editForm.get(['valorTattoDia'])!.value,
      valorRegistrado: this.editForm.get(['valorRegistrado'])!.value,
      diferencia: this.editForm.get(['diferencia'])!.value,
    };
  }
}
