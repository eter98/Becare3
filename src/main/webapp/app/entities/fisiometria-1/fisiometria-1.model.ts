import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IFisiometria1 {
  id?: number;
  ritmoCardiaco?: number | null;
  ritmoRespiratorio?: number | null;
  oximetria?: number | null;
  presionArterialSistolica?: number | null;
  presionArterialDiastolica?: number | null;
  temperatura?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  fechaToma?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class Fisiometria1 implements IFisiometria1 {
  constructor(
    public id?: number,
    public ritmoCardiaco?: number | null,
    public ritmoRespiratorio?: number | null,
    public oximetria?: number | null,
    public presionArterialSistolica?: number | null,
    public presionArterialDiastolica?: number | null,
    public temperatura?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public fechaToma?: dayjs.Dayjs | null,
    public user?: IUser | null
  ) {}
}

export function getFisiometria1Identifier(fisiometria1: IFisiometria1): number | undefined {
  return fisiometria1.id;
}
