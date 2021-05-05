import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface ITokenDisp {
  id?: number;
  tokenConexion?: string | null;
  activo?: boolean | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class TokenDisp implements ITokenDisp {
  constructor(
    public id?: number,
    public tokenConexion?: string | null,
    public activo?: boolean | null,
    public fechaInicio?: dayjs.Dayjs | null,
    public fechaFin?: dayjs.Dayjs | null,
    public user?: IUser | null
  ) {
    this.activo = this.activo ?? false;
  }
}

export function getTokenDispIdentifier(tokenDisp: ITokenDisp): number | undefined {
  return tokenDisp.id;
}
