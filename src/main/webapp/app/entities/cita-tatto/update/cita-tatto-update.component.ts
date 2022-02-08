import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map, startWith } from 'rxjs/operators';
import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICitaTatto, CitaTatto } from '../cita-tatto.model';
import { CitaTattoService } from '../service/cita-tatto.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-cita-tatto-update',
  templateUrl: './cita-tatto-update.component.html',
})
export class CitaTattoUpdateComponent implements OnInit {
  isSaving = false;
  clientes: ICliente[] = [];
  updateCita = false;
  saving = true;
  titulo?: string | undefined;

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    fechaCita: [],
    infoCliente: [],
    hora: [],
    emailCliente: [],
    fotoDiseno: [],
    fotoDisenoContentType: [],
    valorTatto: [],
    valorPagado: [],
    abono: [],
    deuda: [],
    estado: [],
    descripcion: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected citaTattoService: CitaTattoService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected clienteService: ClienteService,
    protected alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ citaTatto }) => {
      if (citaTatto.id === undefined) {
        this.titulo = 'Agendar Cita Tattoo';
        const today = dayjs().startOf('day');
        citaTatto.fechaCreacion = today;
        citaTatto.fechaCita = today;
        this.updateCita = false;
      } else {
        this.titulo = 'Actalizar Cita Tattoo';
        this.updateCita = true;
      }

      this.updateForm(citaTatto);
      this.consultarClientes();
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

  calcularValores(): void {
    const valorTatto = this.editForm.get(['valorTatto'])!.value;
    const valorPagado = this.editForm.get(['valorPagado'])!.value;

    if (valorPagado === 0 || valorPagado === null) {
      this.editForm.get(['deuda'])?.setValue(0);
    } else {
      const valorDeuda = valorTatto - valorPagado;
      this.editForm.get(['deuda'])?.setValue(valorDeuda);
      if (valorDeuda === 0) {
        this.editForm.get(['estado'])?.setValue('Pagada');
      } else {
        this.editForm.get(['estado'])?.setValue('Deuda');
      }
    }

    this.saving = false;
  }

  consultarClientes(): void {
    this.clienteService.query().subscribe(
      (res: HttpResponse<ICliente[]>) => {
        this.clientes = res.body ?? [];
      },
      () => {
        this.clientes = [];
      }
    );
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const citaTatto = this.createFromForm();
    if (citaTatto.id !== undefined) {
      this.subscribeToSaveResponse(this.citaTattoService.update(citaTatto));
    } else {
      this.subscribeToSaveResponse(this.citaTattoService.create(citaTatto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICitaTatto>>): void {
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

  protected updateForm(citaTatto: ICitaTatto): void {
    this.editForm.patchValue({
      id: citaTatto.id,
      fechaCreacion: citaTatto.fechaCreacion ? citaTatto.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      fechaCita: citaTatto.fechaCita ? citaTatto.fechaCita.format(DATE_TIME_FORMAT) : null,
      hora: citaTatto.hora,
      emailCliente: citaTatto.emailCliente,
      fotoDiseno: citaTatto.fotoDiseno,
      fotoDisenoContentType: citaTatto.fotoDisenoContentType,
      valorTatto: citaTatto.valorTatto,
      valorPagado: citaTatto.valorPagado,
      deuda: citaTatto.deuda,
      estado: citaTatto.estado,
      descripcion: citaTatto.descripcion,
      infoCLiente: citaTatto.infoCliente,
    });
  }

  protected createFromForm(): ICitaTatto {
    return {
      ...new CitaTatto(),
      id: this.editForm.get(['id'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaCita: this.editForm.get(['fechaCita'])!.value ? dayjs(this.editForm.get(['fechaCita'])!.value, DATE_TIME_FORMAT) : undefined,
      hora: this.editForm.get(['hora'])!.value,
      emailCliente: this.editForm.get(['emailCliente'])!.value,
      fotoDisenoContentType: this.editForm.get(['fotoDisenoContentType'])!.value,
      fotoDiseno: this.editForm.get(['fotoDiseno'])!.value,
      valorTatto: this.editForm.get(['valorTatto'])!.value,
      valorPagado: this.editForm.get(['valorPagado'])!.value,
      deuda: this.editForm.get(['deuda'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      infoCliente: this.editForm.get(['infoCliente'])!.value,
    };
  }
}
