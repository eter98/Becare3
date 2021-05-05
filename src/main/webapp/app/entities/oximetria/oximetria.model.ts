import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IOximetria {
  id?: number;
  oximetria?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class Oximetria implements IOximetria {
  constructor(
    public id?: number,
    public oximetria?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public user?: IUser | null
  ) {}
}

export function getOximetriaIdentifier(oximetria: IOximetria): number | undefined {
  return oximetria.id;
}
