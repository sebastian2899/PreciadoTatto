import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IGaleriaFotos, GaleriaFotos } from '../galeria-fotos.model';
import { GaleriaFotosService } from '../service/galeria-fotos.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-galeria-fotos-update',
  templateUrl: './galeria-fotos-update.component.html',
})
export class GaleriaFotosUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombreDisenio: [],
    disenio: [],
    disenioContentType: [],
    descripcion: [],
    precioDisenio: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected galeriaFotosService: GaleriaFotosService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ galeriaFotos }) => {
      this.updateForm(galeriaFotos);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('preciadoTattoApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const galeriaFotos = this.createFromForm();
    if (galeriaFotos.id !== undefined) {
      this.subscribeToSaveResponse(this.galeriaFotosService.update(galeriaFotos));
    } else {
      this.subscribeToSaveResponse(this.galeriaFotosService.create(galeriaFotos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGaleriaFotos>>): void {
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

  protected updateForm(galeriaFotos: IGaleriaFotos): void {
    this.editForm.patchValue({
      id: galeriaFotos.id,
      nombreDisenio: galeriaFotos.nombreDisenio,
      disenio: galeriaFotos.disenio,
      disenioContentType: galeriaFotos.disenioContentType,
      descripcion: galeriaFotos.descripcion,
      precioDisenio: galeriaFotos.precioDisenio,
    });
  }

  protected createFromForm(): IGaleriaFotos {
    return {
      ...new GaleriaFotos(),
      id: this.editForm.get(['id'])!.value,
      nombreDisenio: this.editForm.get(['nombreDisenio'])!.value,
      disenioContentType: this.editForm.get(['disenioContentType'])!.value,
      disenio: this.editForm.get(['disenio'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      precioDisenio: this.editForm.get(['precioDisenio'])!.value,
    };
  }
}
