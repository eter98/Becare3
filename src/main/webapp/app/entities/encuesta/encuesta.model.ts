import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IEncuesta {
  id?: number;
  fecha?: dayjs.Dayjs | null;
  debilidad?: boolean | null;
  cefalea?: boolean | null;
  calambres?: boolean | null;
  nauseas?: boolean | null;
  vomito?: boolean | null;
  mareo?: boolean | null;
  ninguna?: boolean | null;
  user?: IUser | null;
}

export class Encuesta implements IEncuesta {
  constructor(
    public id?: number,
    public fecha?: dayjs.Dayjs | null,
    public debilidad?: boolean | null,
    public cefalea?: boolean | null,
    public calambres?: boolean | null,
    public nauseas?: boolean | null,
    public vomito?: boolean | null,
    public mareo?: boolean | null,
    public ninguna?: boolean | null,
    public user?: IUser | null
  ) {
    this.debilidad = this.debilidad ?? false;
    this.cefalea = this.cefalea ?? false;
    this.calambres = this.calambres ?? false;
    this.nauseas = this.nauseas ?? false;
    this.vomito = this.vomito ?? false;
    this.mareo = this.mareo ?? false;
    this.ninguna = this.ninguna ?? false;
  }
}

export function getEncuestaIdentifier(encuesta: IEncuesta): number | undefined {
  return encuesta.id;
}
