import { IPregunta } from 'app/entities/pregunta/pregunta.model';
import { IUser } from 'app/entities/user/user.model';

export interface ICuestionarioEstado {
  id?: number;
  valor?: number | null;
  valoracion?: string | null;
  pregunta?: IPregunta | null;
  user?: IUser | null;
}

export class CuestionarioEstado implements ICuestionarioEstado {
  constructor(
    public id?: number,
    public valor?: number | null,
    public valoracion?: string | null,
    public pregunta?: IPregunta | null,
    public user?: IUser | null
  ) {}
}

export function getCuestionarioEstadoIdentifier(cuestionarioEstado: ICuestionarioEstado): number | undefined {
  return cuestionarioEstado.id;
}
