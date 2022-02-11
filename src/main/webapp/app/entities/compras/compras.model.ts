import * as dayjs from 'dayjs';
import { IProducto } from '../producto/producto.model';

export interface ICompras {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  valorCompra?: number | null;
  valorPagado?: number | null;
  valorDeuda?: number | null;
  estado?: string | null;
  productosSeleccionados?: IProducto[] | null;
  cantidadDisponible?: number | null;
  totalCompra?: number | null;
}

export class Compras implements ICompras {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public valorCompra?: number | null,
    public valorPagado?: number | null,
    public valorDeuda?: number | null,
    public estado?: string | null,
    public productosSeleccionados?: IProducto[] | null,
    public cantidadDisponible?: number | null,
    public totalCompra?: number | null
  ) {}
}

export function getComprasIdentifier(compras: ICompras): number | undefined {
  return compras.id;
}
