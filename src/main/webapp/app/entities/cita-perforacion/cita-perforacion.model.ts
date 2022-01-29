import * as dayjs from 'dayjs';

export interface ICitaPerforacion {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  fechaCita?: dayjs.Dayjs | null;
  hora?: string | null;
  nombreCliente?: string | null;
  valorPerforacion?: number | null;
  valorPagado?: number | null;
  valorDeuda?: number | null;
  estado?: number | null;
}

export class CitaPerforacion implements ICitaPerforacion {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public fechaCita?: dayjs.Dayjs | null,
    public hora?: string | null,
    public nombreCliente?: string | null,
    public valorPerforacion?: number | null,
    public valorPagado?: number | null,
    public valorDeuda?: number | null,
    public estado?: number | null
  ) {}
}

export function getCitaPerforacionIdentifier(citaPerforacion: ICitaPerforacion): number | undefined {
  return citaPerforacion.id;
}
