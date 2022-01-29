import * as dayjs from 'dayjs';

export interface ICitaTatto {
  id?: number;
  idCliente?: number | null;
  fechaCreacion?: dayjs.Dayjs | null;
  fechaCita?: dayjs.Dayjs | null;
  hora?: string | null;
  emailCliente?: string | null;
  fotoDisenoContentType?: string | null;
  fotoDiseno?: string | null;
  valorTatto?: number | null;
  valorPagado?: number | null;
  abono?: number | null;
  deuda?: number | null;
  estado?: string | null;
  descripcion?: string | null;
}

export class CitaTatto implements ICitaTatto {
  constructor(
    public id?: number,
    public idCliente?: number | null,
    public fechaCreacion?: dayjs.Dayjs | null,
    public fechaCita?: dayjs.Dayjs | null,
    public hora?: string | null,
    public emailCliente?: string | null,
    public fotoDisenoContentType?: string | null,
    public fotoDiseno?: string | null,
    public valorTatto?: number | null,
    public valorPagado?: number | null,
    public abono?: number | null,
    public deuda?: number | null,
    public estado?: string | null,
    public descripcion?: string | null
  ) {}
}

export function getCitaTattoIdentifier(citaTatto: ICitaTatto): number | undefined {
  return citaTatto.id;
}
