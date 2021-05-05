import * as dayjs from 'dayjs';
import { IMedicamento } from 'app/entities/medicamento/medicamento.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';

export interface IAdherencia {
  id?: number;
  horaToma?: dayjs.Dayjs | null;
  respuesta?: boolean | null;
  valor?: number | null;
  comentario?: string | null;
  medicamento?: IMedicamento | null;
  paciente?: IPaciente | null;
}

export class Adherencia implements IAdherencia {
  constructor(
    public id?: number,
    public horaToma?: dayjs.Dayjs | null,
    public respuesta?: boolean | null,
    public valor?: number | null,
    public comentario?: string | null,
    public medicamento?: IMedicamento | null,
    public paciente?: IPaciente | null
  ) {
    this.respuesta = this.respuesta ?? false;
  }
}

export function getAdherenciaIdentifier(adherencia: IAdherencia): number | undefined {
  return adherencia.id;
}
