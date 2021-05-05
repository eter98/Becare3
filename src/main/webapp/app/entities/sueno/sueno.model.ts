import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface ISueno {
  id?: number;
  superficial?: number | null;
  profundo?: number | null;
  despierto?: number | null;
  timeInstant?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class Sueno implements ISueno {
  constructor(
    public id?: number,
    public superficial?: number | null,
    public profundo?: number | null,
    public despierto?: number | null,
    public timeInstant?: dayjs.Dayjs | null,
    public user?: IUser | null
  ) {}
}

export function getSuenoIdentifier(sueno: ISueno): number | undefined {
  return sueno.id;
}
