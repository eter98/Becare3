import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IPeso {
  id?: number;
  pesoKG?: number | null;
  descripcion?: string | null;
  fechaRegistro?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class Peso implements IPeso {
  constructor(
    public id?: number,
    public pesoKG?: number | null,
    public descripcion?: string | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public user?: IUser | null
  ) {}
}

export function getPesoIdentifier(peso: IPeso): number | undefined {
  return peso.id;
}
