import { ICondicion } from 'app/entities/condicion/condicion.model';

export interface IPregunta {
  id?: number;
  pregunta?: string | null;
  condicion?: ICondicion | null;
}

export class Pregunta implements IPregunta {
  constructor(public id?: number, public pregunta?: string | null, public condicion?: ICondicion | null) {}
}

export function getPreguntaIdentifier(pregunta: IPregunta): number | undefined {
  return pregunta.id;
}
