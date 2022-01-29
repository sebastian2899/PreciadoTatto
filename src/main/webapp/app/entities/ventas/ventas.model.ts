export interface IVentas {
  id?: number;
  fechaCreacion?: string | null;
  idCliente?: number | null;
  valorVenta?: number | null;
  valorPagado?: number | null;
  valorDeuda?: number | null;
  estado?: string | null;
}

export class Ventas implements IVentas {
  constructor(
    public id?: number,
    public fechaCreacion?: string | null,
    public idCliente?: number | null,
    public valorVenta?: number | null,
    public valorPagado?: number | null,
    public valorDeuda?: number | null,
    public estado?: string | null
  ) {}
}

export function getVentasIdentifier(ventas: IVentas): number | undefined {
  return ventas.id;
}
