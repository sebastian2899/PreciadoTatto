import * as dayjs from 'dayjs';

export interface ICajaTattos {
  id?: number;
  valorTattoDia?: number | null;
  valorRegistrado?: number | null;
  diferencia?: number | null;
  fechaCreacion?: dayjs.Dayjs | null;
}

export class CajaTattos implements ICajaTattos {
  constructor(
    public id?: number,
    public valorTattoDia?: number | null,
    public valorRegistrado?: number | null,
    public diferencia?: number | null,
    public fechaCreacion?: dayjs.Dayjs | null
  ) {}
}

export function getCajaTattosIdentifier(cajaTattos: ICajaTattos): number | undefined {
  return cajaTattos.id;
}
