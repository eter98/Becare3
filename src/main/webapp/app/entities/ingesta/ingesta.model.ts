import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IIngesta {
  id?: number;
  tipo?: string | null;
  consumoCalorias?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class Ingesta implements IIngesta {
  constructor(
    public id?: number,
    public tipo?: string | null,
    public consumoCalorias?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public user?: IUser | null
  ) {}
}

export function getIngestaIdentifier(ingesta: IIngesta): number | undefined {
  return ingesta.id;
}
