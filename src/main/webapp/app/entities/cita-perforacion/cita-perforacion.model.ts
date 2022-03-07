import * as dayjs from 'dayjs';

export interface ICitaPerforacion {
  id?: number;
  fechaCreacionInicial?: dayjs.Dayjs | null;
  fechaCreacion?: dayjs.Dayjs | null;
  fechaCita?: dayjs.Dayjs | null;
  hora?: string | null;
  tipoCita?: string | null;
  valorTotalDescuento?: number | null;
  valorCaja?: number | null;
  nombreCliente?: string | null;
  valorPerforacion?: number | null;
  valorPagado?: number | null;
  valorDeuda?: number | null;
  estado?: string | null;
}

export class CitaPerforacion implements ICitaPerforacion {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public fechaCita?: dayjs.Dayjs | null,
    public hora?: string | null,
    public nombreCliente?: string | null,
    public tipoCita?: string | null,
    public valorTotalDesuento?: number | null,
    public valorPerforacion?: number | null,
    public valorCaja?: number | null,
    public valorPagado?: number | null,
    public valorDeuda?: number | null,
    public estado?: string | null
  ) {}
}

export function getCitaPerforacionIdentifier(citaPerforacion: ICitaPerforacion): number | undefined {
  return citaPerforacion.id;
}
