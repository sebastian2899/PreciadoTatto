import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICajaTattos, CajaTattos } from '../caja-tattos.model';
import { CajaTattosService } from '../service/caja-tattos.service';
import { ICliente } from 'app/entities/cliente/cliente.model';

@Component({
  selector: 'jhi-caja-tattos-update',
  templateUrl: './caja-tattos-update.component.html',
})
export class CajaTattosUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    valorTattoDia: [],
    valorRegistrado: [],
    diferencia: [],
  });

  constructor(protected cajaTattosService: CajaTattosService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cajaTattos }) => {
      this.updateForm(cajaTattos);
    });
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
