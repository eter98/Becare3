import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface ICaloria {
  id?: number;
  caloriasActivas?: number | null;
  descripcion?: string | null;
  fechaRegistro?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class Caloria implements ICaloria {
  constructor(
    public id?: number,
    public caloriasActivas?: number | null,
    public descripcion?: string | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public user?: IUser | null
  ) {}
}

export function getCaloriaIdentifier(caloria: ICaloria): number | undefined {
  return caloria.id;
}
