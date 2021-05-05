import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IPrograma {
  id?: number;
  caloriasActividad?: number | null;
  pasosActividad?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class Programa implements IPrograma {
  constructor(
    public id?: number,
    public caloriasActividad?: number | null,
    public pasosActividad?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public user?: IUser | null
  ) {}
}

export function getProgramaIdentifier(programa: IPrograma): number | undefined {
  return programa.id;
}
