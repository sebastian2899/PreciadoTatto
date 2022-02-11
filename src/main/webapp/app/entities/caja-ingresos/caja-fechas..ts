export interface ICajaFechaIngresos {
  id?: number;
  valorVendido?: number | null;
  valorPagado?: number | null;
  diferencia?: number | null;
}

export class CajaFechaIngresos implements ICajaFechaIngresos {
  constructor(
    public id?: number,
    public valorVendido?: number | null,
    public valorPagado?: number | null,
    public diferenticia?: number | null
  ) {}
}

export function getCajaFechaIngresosIdentifier(cajaFechaIngresos: ICajaFechaIngresos): number | undefined {
  return cajaFechaIngresos.id;
}
