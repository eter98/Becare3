import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IPresionSanguinea {
  id?: number;
  presionSanguineaSistolica?: number | null;
  presionSanguineaDiastolica?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class PresionSanguinea implements IPresionSanguinea {
  constructor(
    public id?: number,
    public presionSanguineaSistolica?: number | null,
    public presionSanguineaDiastolica?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public user?: IUser | null
  ) {}
}

export function getPresionSanguineaIdentifier(presionSanguinea: IPresionSanguinea): number | undefined {
  return presionSanguinea.id;
}
