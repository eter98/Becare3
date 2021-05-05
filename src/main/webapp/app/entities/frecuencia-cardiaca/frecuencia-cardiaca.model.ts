import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IFrecuenciaCardiaca {
  id?: number;
  frecuenciaCardiaca?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class FrecuenciaCardiaca implements IFrecuenciaCardiaca {
  constructor(
    public id?: number,
    public frecuenciaCardiaca?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public user?: IUser | null
  ) {}
}

export function getFrecuenciaCardiacaIdentifier(frecuenciaCardiaca: IFrecuenciaCardiaca): number | undefined {
  return frecuenciaCardiaca.id;
}
