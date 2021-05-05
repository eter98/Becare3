import { ITratamieto } from 'app/entities/tratamieto/tratamieto.model';
import { IMedicamento } from 'app/entities/medicamento/medicamento.model';

export interface ITratamientoMedicamento {
  id?: number;
  dosis?: string | null;
  intensidad?: string | null;
  tratamieto?: ITratamieto | null;
  medicamento?: IMedicamento | null;
}

export class TratamientoMedicamento implements ITratamientoMedicamento {
  constructor(
    public id?: number,
    public dosis?: string | null,
    public intensidad?: string | null,
    public tratamieto?: ITratamieto | null,
    public medicamento?: IMedicamento | null
  ) {}
}

export function getTratamientoMedicamentoIdentifier(tratamientoMedicamento: ITratamientoMedicamento): number | undefined {
  return tratamientoMedicamento.id;
}
