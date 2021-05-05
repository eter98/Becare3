import { IUser } from 'app/entities/user/user.model';

export interface IDispositivo {
  id?: number;
  dispositivo?: string | null;
  mac?: string | null;
  conectado?: boolean | null;
  user?: IUser | null;
}

export class Dispositivo implements IDispositivo {
  constructor(
    public id?: number,
    public dispositivo?: string | null,
    public mac?: string | null,
    public conectado?: boolean | null,
    public user?: IUser | null
  ) {
    this.conectado = this.conectado ?? false;
  }
}

export function getDispositivoIdentifier(dispositivo: IDispositivo): number | undefined {
  return dispositivo.id;
}
