export interface ICondicion {
  id?: number;
  condicion?: string | null;
  descripcion?: string | null;
}

export class Condicion implements ICondicion {
  constructor(public id?: number, public condicion?: string | null, public descripcion?: string | null) {}
}

export function getCondicionIdentifier(condicion: ICondicion): number | undefined {
  return condicion.id;
}
