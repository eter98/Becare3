export interface IFarmaceutica {
  id?: number;
  nombre?: string | null;
  direccion?: string | null;
  propietario?: string | null;
}

export class Farmaceutica implements IFarmaceutica {
  constructor(public id?: number, public nombre?: string | null, public direccion?: string | null, public propietario?: string | null) {}
}

export function getFarmaceuticaIdentifier(farmaceutica: IFarmaceutica): number | undefined {
  return farmaceutica.id;
}
