import { IMedicamento } from 'app/entities/medicamento/medicamento.model';

export interface IAgenda {
  id?: number;
  horaMedicamento?: number | null;
  medicamento?: IMedicamento | null;
}

export class Agenda implements IAgenda {
  constructor(public id?: number, public horaMedicamento?: number | null, public medicamento?: IMedicamento | null) {}
}

export function getAgendaIdentifier(agenda: IAgenda): number | undefined {
  return agenda.id;
}
