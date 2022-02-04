export interface ICajaFechasTattoos {
  id?: number;
  valorVendido?: number | null;
  valorPagado?: number | null;
  diferencia?: number | null;
}

export class CajaFechasTattos implements ICajaFechasTattoos {
  constructor(
    public id?: number,
    public valorVendido?: number | null,
    public valorPagado?: number | null,
    public diferencia?: number | null
  ) {}
}

export function getCajaFechasTattosIdentifier(cajaFechasTattos: ICajaFechasTattoos): number | undefined {
  return cajaFechasTattos.id;
}
