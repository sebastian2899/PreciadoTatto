export interface IProducto {
  id?: number;
  fotoContentType?: string | null;
  foto?: string | null;
  nombre?: string | null;
  precio?: number | null;
  cantidad?: number | null;
  descripcion?: string | null;
  valorTotal?: number | null;
  cantidadDisponible?: number | null;
}

export class Producto implements IProducto {
  constructor(
    public id?: number,
    public fotoContentType?: string | null,
    public foto?: string | null,
    public nombre?: string | null,
    public precio?: number | null,
    public cantidad?: number | null,
    public descripcion?: string | null,
    public valorTotal?: number | null,
    public cantidadDisponible?: number | null
  ) {}
}

export function getProductoIdentifier(producto: IProducto): number | undefined {
  return producto.id;
}
