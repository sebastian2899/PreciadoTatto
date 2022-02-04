import * as dayjs from 'dayjs';
import { IProducto } from '../producto/producto.model';

export interface IVentas {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  idCliente?: number | null;
  valorVenta?: number | null;
  valorPagado?: number | null;
  valorDeuda?: number | null;
  estado?: string | null;
  productosSeleccionados?: IProducto[] | null;
}

export class Ventas implements IVentas {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public idCliente?: number | null,
    public valorVenta?: number | null,
    public valorPagado?: number | null,
    public valorDeuda?: number | null,
    public estado?: string | null,
    public productosSeleccionados?: IProducto[] | null
  ) {}
}

export function getVentasIdentifier(ventas: IVentas): number | undefined {
  return ventas.id;
}
