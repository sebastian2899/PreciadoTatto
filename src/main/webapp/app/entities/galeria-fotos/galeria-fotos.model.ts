export interface IGaleriaFotos {
  id?: number;
  nombreDisenio?: string | null;
  disenioContentType?: string | null;
  disenio?: string | null;
  descripcion?: string | null;
  precioDisenio?: number | null;
}

export class GaleriaFotos implements IGaleriaFotos {
  constructor(
    public id?: number,
    public nombreDisenio?: string | null,
    public disenioContentType?: string | null,
    public disenio?: string | null,
    public descripcion?: string | null,
    public precioDisenio?: number | null
  ) {}
}

export function getGaleriaFotosIdentifier(galeriaFotos: IGaleriaFotos): number | undefined {
  return galeriaFotos.id;
}
