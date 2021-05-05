import * as dayjs from 'dayjs';

export interface ITratamieto {
  id?: number;
  descripcionTratamiento?: string | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
}

export class Tratamieto implements ITratamieto {
  constructor(
    public id?: number,
    public descripcionTratamiento?: string | null,
    public fechaInicio?: dayjs.Dayjs | null,
    public fechaFin?: dayjs.Dayjs | null
  ) {}
}

export function getTratamietoIdentifier(tratamieto: ITratamieto): number | undefined {
  return tratamieto.id;
}
