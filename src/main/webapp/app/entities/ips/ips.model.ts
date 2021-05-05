export interface IIPS {
  id?: number;
  nombre?: string | null;
  nit?: string | null;
  direccion?: string | null;
  telefono?: string | null;
  correoElectronico?: string | null;
}

export class IPS implements IIPS {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public nit?: string | null,
    public direccion?: string | null,
    public telefono?: string | null,
    public correoElectronico?: string | null
  ) {}
}

export function getIPSIdentifier(iPS: IIPS): number | undefined {
  return iPS.id;
}
