import * as dayjs from 'dayjs';

export interface IAbono {
  id?: number;
  fechaAbono?: dayjs.Dayjs | null;
  idCita?: number | null;
  valorAbono?: number | null;
}

export class Abono implements IAbono {
  constructor(
    public id?: number,
    public fechaAbono?: dayjs.Dayjs | null,
    public idCita?: number | null,
    public valorAbono?: number | null
  ) {}
}

export function getAbonoIdentifier(abono: IAbono): number | undefined {
  return abono.id;
}
