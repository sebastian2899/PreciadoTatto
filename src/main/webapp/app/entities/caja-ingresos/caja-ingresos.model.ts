import * as dayjs from 'dayjs';

export interface ICajaIngresos {
  id?: number;
  valorVendidoDia?: number | null;
  valorRegistradoDia?: number | null;
  diferencia?: number | null;
  fechaCreacion?: dayjs.Dayjs | null;
}

export class CajaIngresos implements ICajaIngresos {
  constructor(
    public id?: number,
    public valorVendidoDia?: number | null,
    public valorRegistradoDia?: number | null,
    public diferencia?: number | null,
    public fechaCreacion?: dayjs.Dayjs | null
  ) {}
}

export function getCajaIngresosIdentifier(cajaIngresos: ICajaIngresos): number | undefined {
  return cajaIngresos.id;
}
