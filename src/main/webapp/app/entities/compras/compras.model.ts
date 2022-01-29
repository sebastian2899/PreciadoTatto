import * as dayjs from 'dayjs';

export interface ICompras {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  valorCompra?: number | null;
  valorPagado?: number | null;
  valorDeuda?: number | null;
  estado?: string | null;
}

export class Compras implements ICompras {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public valorCompra?: number | null,
    public valorPagado?: number | null,
    public valorDeuda?: number | null,
    public estado?: string | null
  ) {}
}

export function getComprasIdentifier(compras: ICompras): number | undefined {
  return compras.id;
}
