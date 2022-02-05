import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IVentas, Ventas } from '../ventas.model';
import { VentasService } from '../service/ventas.service';
import { IProducto } from 'app/entities/producto/producto.model';
import { AlertService } from 'app/core/util/alert.service';
import { ProductoService } from 'app/entities/producto/service/producto.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

@Component({
  selector: 'jhi-ventas-update',
  templateUrl: './ventas-update.component.html',
})
export class VentasUpdateComponent implements OnInit {
  @ViewChild('mensajeInicio', { static: true }) content: ElementRef | undefined;
  @ViewChild('mensjaecant', { static: true }) content2: ElementRef | undefined;
  @ViewChild('mensajeVenta', { static: true }) content3: ElementRef | undefined;

  titulo?: string | undefined;
  isSaving = false;
  productos?: IProducto[] = [];
  productosSeleccionados: IProducto[] = [];
  productoSeleccionado?: IProducto | null;
  productoValores?: IProducto | null;
  cantidad?: number | null;
  nombre?: string | null;
  cantSuma?: number | null;
  cantidadCero = false;
  validarCantidad = true;
  mensajeCantidad?: string | null;
  modalCantidad?: boolean | null;
  validarFactura = true;
  clientes: ICliente[] = [];
  validarVenta = false;

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    valorVenta: [],
    valorPagado: [],
    valorDeuda: [],
    estado: [],
    producto: new FormControl(),
    cantidad: [],
  });

  constructor(
    private alert: AlertService,
    protected ventasService: VentasService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modal: NgbModal,
    private productoService: ProductoService,
    private clienteService: ClienteService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ventas }) => {
      if (ventas.id !== undefined) {
        this.titulo = 'Editar Venta';
        this.productosSeleccionados = ventas.productosSeleccioandos;
      } else {
        this.titulo = 'Crear Venta';
      }

      this.updateForm(ventas);
    });
    this.consultarProductos();
    this.consultarCliente();
    this.modal.open(this.content);
  }

  previousState(): void {
    window.history.back();
  }

  consultarProductos(): void {
    this.productoService.disponibles().subscribe(
      (res: HttpResponse<IProducto[]>) => {
        this.productos = res.body ?? [];
      },
      () => {
        this.productos = [];
      }
    );
  }

  save(): void {
    this.isSaving = true;
    const ventas = this.createFromForm();
    if (ventas.id !== undefined) {
      this.subscribeToSaveResponse(this.ventasService.update(ventas));
    } else {
      ventas.productosSeleccionados = this.productosSeleccionados;
      this.subscribeToSaveResponse(this.ventasService.create(ventas));
    }
  }

  consultarCliente(): void {
    this.clienteService.query().subscribe(
      (res: HttpResponse<ICliente[]>) => {
        this.clientes = res.body ?? [];
      },
      () => {
        this.clientes = [];
      }
    );
  }

  validarCant(): void {
    const cantidadForm = this.editForm.get(['cantidad'])!.value;
    if (cantidadForm === 0 || cantidadForm === null || cantidadForm === undefined || cantidadForm < 0) {
      this.cantidadCero = true;
      this.validarCantidad = true;
      this.modal.open(this.content);
    } else {
      this.cantidadCero = false;
    }
  }

  cantProducto(cant?: number, producto?: IProducto): void {
    const idProducto = this.editForm.get(['producto'])!.value;
    const cantidadForm = this.editForm.get(['cantidad'])!.value;
    let resp = false;

    this.productoService.find(idProducto).subscribe((res: HttpResponse<IProducto>) => {
      this.productoValores = res.body;
      this.cantidad = this.productoValores?.cantidad;
      this.nombre = this.productoValores?.nombre;

      if (this.cantidad && this.productoValores) {
        if (cantidadForm > this.cantidad || (cant! > this.cantidad ? (resp = true) : (resp = false)) || this.cantidad === 0) {
          if (resp) {
            this.eliminarProductoSeleccionado(producto!);
          }
          this.mensajeCantidad = `No se puede seleccionar el producto ${String(this.productoValores.nombre)}, ya
            que se esta seleccionando ${String(cantidadForm)} y solo hay ${String(this.productoValores.cantidad)} productos
            disponibles.`;
          this.modal.open(this.content2);
          this.modalCantidad = true;
          this.validarCantidad = true;
        } else {
          if (!producto) {
            this.modalCantidad = false;
            this.alert.addAlert({
              type: 'info',
              message: 'Cantidad disponible.',
            });
            this.modalCantidad = false;
            this.validarCantidad = false;
          }
        }
      }
    });
  }

  eliminarProductoSeleccionado(producto: IProducto): void {
    const index = this.productosSeleccionados.indexOf(producto);
    if (index >= 0) {
      this.productosSeleccionados.splice(index, 1);
    }
  }

  agregarProducto(): void {
    this.productos?.forEach(element => {
      const id = this.editForm.get(['producto'])!.value;
      if (element.id === Number(id)) {
        this.productoSeleccionado = element;
      }
    });

    const idActual = this.productoSeleccionado?.id;

    if (this.productosSeleccionados.length > 0) {
      this.cantSuma = this.editForm.get(['cantidad'])!.value;

      for (let i = 0; i < this.productosSeleccionados.length; i++) {
        if (this.productosSeleccionados[i].id === idActual) {
          if (this.productoSeleccionado?.cantidad && this.cantSuma && this.productoSeleccionado.precio) {
            this.productoSeleccionado.cantidad += this.cantSuma;
            this.cantProducto(this.productoSeleccionado.cantidad, this.productoSeleccionado);
            this.productoSeleccionado.valorTotal = this.productoSeleccionado.cantidad * this.productoSeleccionado.precio;
            this.validarCantidad = true;
          }
          break;
        }
      }
    }

    if (this.productoSeleccionado && this.productosSeleccionados.includes(this.productoSeleccionado) === false) {
      this.productoSeleccionado.cantidad = this.editForm.get(['cantidad'])!.value;
      if (this.productoSeleccionado.cantidad && this.productoSeleccionado.precio) {
        this.productoSeleccionado.cantidadDisponible = this.cantidad;
        this.productoSeleccionado.valorTotal = this.productoSeleccionado.precio * this.productoSeleccionado.cantidad;
      }

      this.productosSeleccionados.push(this.productoSeleccionado);
    }

    this.calcularValores();
    this.editForm.get(['producto'])?.setValue(null);
    this.editForm.get(['cantidad'])?.setValue(null);

    this.validarFactura = false;
  }

  calcularValores(): void {
    let valorventa = 0;
    this.productosSeleccionados.forEach(element => {
      if (element.valorTotal) {
        valorventa += element.valorTotal;
      }
    });

    this.editForm.get(['valorVenta'])?.setValue(valorventa);
    this.editForm.get(['valorDeuda'])?.setValue(valorventa);
    this.editForm.get(['estado'])?.setValue('Deuda');
  }

  restarValores(): void {
    this.validarVenta = false;
    const valorVenta = this.editForm.get(['valorVenta'])!.value;
    const valorPagado = this.editForm.get(['valorPagado'])!.value;

    if (valorVenta && valorPagado) {
      const valorDeuda = valorVenta - valorPagado;
      this.editForm.get(['valorDeuda'])?.setValue(valorDeuda);
      this.editForm.get(['estado'])?.setValue('Deuda');

      if (valorPagado > valorVenta) {
        this.validarVenta = true;
        this.modal.open(this.content3);
        this.editForm.get(['valorDeuda'])?.setValue(null);
        this.editForm.get(['estado'])?.setValue('Pagada');
        this.editForm.get(['valorPagado'])?.setValue(null);
      } else if (valorDeuda === 0) {
        this.editForm.get(['valorDeuda'])?.setValue(0);
        this.editForm.get(['estado'])?.setValue('Pagada');
        this.validarVenta = false;
      }
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
      valorVenta: this.editForm.get(['valorVenta'])!.value,
      valorPagado: this.editForm.get(['valorPagado'])!.value,
      valorDeuda: this.editForm.get(['valorDeuda'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
