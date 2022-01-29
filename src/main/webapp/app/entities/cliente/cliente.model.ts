export interface ICliente {
  id?: number;
  nombre?: string | null;
  numeroTelefono?: string | null;
}

export class Cliente implements ICliente {
  constructor(public id?: number, public nombre?: string | null, public numeroTelefono?: string | null) {}
}

export function getClienteIdentifier(cliente: ICliente): number | undefined {
  return cliente.id;
}
