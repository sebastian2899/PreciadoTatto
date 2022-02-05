import * as dayjs from 'dayjs';

export interface ICitaTatto {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  fechaCita?: dayjs.Dayjs | null;
  hora?: string | null;
  emailCliente?: string | null;
  fotoDisenoContentType?: string | null;
  fotoDiseno?: string | null;
  valorTatto?: number | null;
  valorPagado?: number | null;
  deuda?: number | null;
  estado?: string | null;
  descripcion?: string | null;
  nombreCliente?: string | null;
  infoCliente?: string | null;
}

export class CitaTatto implements ICitaTatto {
  constructor(
    public id?: number,
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
    public descripcion?: string | null,
    public nombreCliente?: string | null,
    public infoCliente?: string | null
  ) {}
}

export function getCitaTattoIdentifier(citaTatto: ICitaTatto): number | undefined {
  return citaTatto.id;
}
