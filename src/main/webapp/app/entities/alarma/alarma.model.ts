import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IAlarma {
  id?: number;
  timeInstant?: dayjs.Dayjs | null;
  descripcion?: string | null;
  procedimiento?: string | null;
  titulo?: string | null;
  verificar?: boolean | null;
  observaciones?: string | null;
  prioridad?: string | null;
  user?: IUser | null;
}

export class Alarma implements IAlarma {
  constructor(
    public id?: number,
    public timeInstant?: dayjs.Dayjs | null,
    public descripcion?: string | null,
    public procedimiento?: string | null,
    public titulo?: string | null,
    public verificar?: boolean | null,
    public observaciones?: string | null,
    public prioridad?: string | null,
    public user?: IUser | null
  ) {
    this.verificar = this.verificar ?? false;
  }
}

export function getAlarmaIdentifier(alarma: IAlarma): number | undefined {
  return alarma.id;
}
