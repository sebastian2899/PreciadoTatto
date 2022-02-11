import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICompras, Compras } from '../compras.model';
import { ComprasService } from '../service/compras.service';
import { ProductoService } from 'app/entities/producto/service/producto.service';
import { IProducto } from 'app/entities/producto/producto.model';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-compras-update',
  templateUrl: './compras-update.component.html',
})
export class ComprasUpdateComponent implements OnInit {
  isSaving = false;
  productos?: IProducto[] = [];
  productoSeleccionado?: IProducto | null;
  productosSeleccionados: IProducto[] = [];
  productoBuscado?: IProducto | null;
  titulo?: string | undefined;

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    valorCompra: [],
    valorPagado: [],
    producto: new FormControl(),
    cantidad: [],
    valorDeuda: [],
    estado: [],
  });

  constructor(
    protected comprasService: ComprasService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected productoService: ProductoService,
    protected alert: AlertService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compras }) => {
      if (compras.id === undefined) {
        this.titulo = 'Crear Compra';
        const today = dayjs().startOf('day');
        compras.fechaCreacion = today;
      } else {
        this.titulo = 'Actualizar Compra';
      }

      this.consultsarProductos();
      this.updateForm(compras);
    });
  }

  previousState(): void {
    window.history.back();
  }

  consultsarProductos(): void {
    this.productoService.query().subscribe(
      (res: HttpResponse<IProducto[]>) => {
        this.productos = res.body ?? [];
      },
      () => {
        this.productos = [];
      }
    );
  }

  agregarProducto(): void {
    const producto = this.editForm.get(['producto'])!.value;
    this.productos?.forEach(element => {
      if (element.id === Number(producto)) {
        this.productoSeleccionado = element;
      }
    });

    if (producto) {
      this.productoService.find(producto).subscribe((res: HttpResponse<IProducto>) => {
        this.productoBuscado = res.body;
        this.productoSeleccionado!.cantidadDisponible = this.productoBuscado?.cantidad;
      });
    }

    const idActual = producto;

    if (this.productosSeleccionados.length > 0) {
      for (let i = 0; i < this.productosSeleccionados.length; i++) {
        if (this.productosSeleccionados[i].id === Number(idActual)) {
          const cantSum = this.editForm.get(['cantidad'])!.value;

          this.productoSeleccionado!.cantidad += cantSum;
          if (this.productoSeleccionado!.precio && this.productoSeleccionado!.cantidad) {
            this.productoSeleccionado!.valorTotal = this.productoSeleccionado!.precio * this.productoSeleccionado!.cantidad;
          }

          break;
        }
      }
    }

    if (this.productoSeleccionado && this.productosSeleccionados.includes(this.productoSeleccionado) === false) {
      const cantidad = this.editForm.get(['cantidad'])!.value;
      if (cantidad === 0) {
        this.alert.addAlert({
          type: 'danger',
          message: 'No puede agregar un producto con una cantidad de 0.',
        });
      } else {
        this.productoSeleccionado.cantidad = cantidad;
        if (this.productoSeleccionado.cantidad && this.productoSeleccionado.precio) {
          this.productoSeleccionado.valorTotal = this.productoSeleccionado.precio * this.productoSeleccionado.cantidad;
        }

        this.productosSeleccionados.push(this.productoSeleccionado);
      }
    }

    this.editForm.get(['producto'])?.setValue(null);
    this.editForm.get(['cantidad'])?.setValue(null);
    this.calcularValores();
  }

  eliminarProducto(producto: IProducto): void {
    const index = this.productosSeleccionados.indexOf(producto);
    const valorCompra = this.editForm.get(['valorCompra'])!.value;

    if (index >= 0) {
      this.productosSeleccionados.splice(index, 1);
      this.alert.addAlert({
        type: 'success',
        message: 'Producto eliminado con exito.',
      });
    }

    const valorNuevo = valorCompra - producto.valorTotal!;
    this.editForm.get(['valorCompra'])?.setValue(valorNuevo);
    this.editForm.get(['valorDeuda'])?.setValue(valorNuevo);
  }

  calcularValores(): void {
    let valorCompra = 0;
    this.productosSeleccionados.forEach(element => {
      valorCompra += element.valorTotal!;
    });
    this.editForm.get(['valorCompra'])?.setValue(valorCompra);
    this.editForm.get(['valorDeuda'])?.setValue(valorCompra);
    this.editForm.get(['estado'])?.setValue('Deuda');
  }

  restarValores(): void {
    const valorCompra = this.editForm.get(['valorCompra'])!.value;
    const valorPagado = this.editForm.get(['valorPagado'])!.value;

    if (valorPagado > valorCompra) {
      this.alert.addAlert({
        type: 'danger',
        message: 'El valor a pagar no debe ser mayor al valor de la compra.',
      });
      this.editForm.get(['valorDeuda'])?.setValue(valorCompra);
      this.editForm.get(['valorPagado'])?.setValue(0);
    } else {
      const deuda = valorCompra - valorPagado;
      this.editForm.get(['valorDeuda'])?.setValue(deuda);

      let mensaje = null;
      deuda === 0 ? (mensaje = 'Pagada') : (mensaje = 'Deuda');
      this.editForm.get(['estado'])?.setValue(mensaje);
    }
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
