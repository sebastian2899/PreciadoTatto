import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProducto, Producto } from '../producto.model';
import { ProductoService } from '../service/producto.service';
import { ProductoDeleteDialogComponent } from '../delete/producto-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-producto',
  templateUrl: './producto.component.html',
})
export class ProductoComponent implements OnInit {
  @ViewChild('mensajeValidacion', { static: true }) content: ElementRef | undefined;

  productos?: IProducto[];
  producto?: IProducto;
  isLoading = false;
  mensaje?: string | null;
  productoNombre = '';

  constructor(protected productoService: ProductoService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.producto = new Producto();
    this.productoService.productosFiltro(this.producto).subscribe(
      (res: HttpResponse<IProducto[]>) => {
        this.isLoading = false;
        this.productos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  productosPorFiltro(): void {
    this.producto = new Producto();
    this.isLoading = true;
    this.producto.nombre = this.productoNombre;
    this.productoService.productosFiltro(this.producto).subscribe(
      (res: HttpResponse<IProducto[]>) => {
        this.productos = res.body ?? [];
        this.isLoading = false;
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  validarEliminacion(producto: IProducto): void {
    const id = producto.id;
    const nombre = producto.nombre;
    let resp = null;
    this.productoService.productosPorVenta(id!).subscribe((res: HttpResponse<boolean>) => {
      resp = res.body;
      if (resp) {
        this.mensaje = `No se puede eliminar el producto ${String(nombre)}, ya que este producto esta relacionado con ventas ya creadas.`;
        this.modalService.open(this.content);
      } else {
        this.delete(producto);
      }
    });
  }

  trackId(index: number, item: IProducto): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(producto: IProducto): void {
    const modalRef = this.modalService.open(ProductoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.producto = producto;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
