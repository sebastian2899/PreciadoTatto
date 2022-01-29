import * as dayjs from 'dayjs';

export interface IEgreso {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  descripcion?: string | null;
  valor?: number | null;
}

export class Egreso implements IEgreso {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public descripcion?: string | null,
    public valor?: number | null
  ) {}
}

export function getEgresoIdentifier(egreso: IEgreso): number | undefined {
  return egreso.id;
}
