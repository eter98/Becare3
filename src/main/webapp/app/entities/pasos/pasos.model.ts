import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IPasos {
  id?: number;
  nroPasos?: number | null;
  timeInstant?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class Pasos implements IPasos {
  constructor(public id?: number, public nroPasos?: number | null, public timeInstant?: dayjs.Dayjs | null, public user?: IUser | null) {}
}

export function getPasosIdentifier(pasos: IPasos): number | undefined {
  return pasos.id;
}
