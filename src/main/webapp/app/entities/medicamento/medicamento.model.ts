import * as dayjs from 'dayjs';
import { Presentacion } from 'app/entities/enumerations/presentacion.model';

export interface IMedicamento {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  fechaIngreso?: dayjs.Dayjs | null;
  presentacion?: Presentacion | null;
  generico?: string | null;
}

export class Medicamento implements IMedicamento {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public descripcion?: string | null,
    public fechaIngreso?: dayjs.Dayjs | null,
    public presentacion?: Presentacion | null,
    public generico?: string | null
  ) {}
}

export function getMedicamentoIdentifier(medicamento: IMedicamento): number | undefined {
  return medicamento.id;
}
