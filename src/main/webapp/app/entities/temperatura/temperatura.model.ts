import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface ITemperatura {
  id?: number;
  temperatura?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class Temperatura implements ITemperatura {
  constructor(
    public id?: number,
    public temperatura?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public user?: IUser | null
  ) {}
}

export function getTemperaturaIdentifier(temperatura: ITemperatura): number | undefined {
  return temperatura.id;
}
