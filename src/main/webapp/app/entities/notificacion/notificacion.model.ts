import * as dayjs from 'dayjs';
import { ITokenDisp } from 'app/entities/token-disp/token-disp.model';

export interface INotificacion {
  id?: number;
  fechaInicio?: dayjs.Dayjs | null;
  fechaActualizacion?: dayjs.Dayjs | null;
  estado?: number | null;
  tipoNotificacion?: number | null;
  token?: ITokenDisp | null;
}

export class Notificacion implements INotificacion {
  constructor(
    public id?: number,
    public fechaInicio?: dayjs.Dayjs | null,
    public fechaActualizacion?: dayjs.Dayjs | null,
    public estado?: number | null,
    public tipoNotificacion?: number | null,
    public token?: ITokenDisp | null
  ) {}
}

export function getNotificacionIdentifier(notificacion: INotificacion): number | undefined {
  return notificacion.id;
}
