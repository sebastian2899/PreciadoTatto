export interface IMensajeValidacionCita {
  mensaje?: string | null;
}

export class MensajeValidacionCita implements IMensajeValidacionCita {
  constructor(public mensaje?: string | null) {}
}
