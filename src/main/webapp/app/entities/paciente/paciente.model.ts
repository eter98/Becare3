import { ICondicion } from 'app/entities/condicion/condicion.model';
import { IIPS } from 'app/entities/ips/ips.model';
import { IUser } from 'app/entities/user/user.model';
import { ITratamieto } from 'app/entities/tratamieto/tratamieto.model';
import { IFarmaceutica } from 'app/entities/farmaceutica/farmaceutica.model';
import { Identificaciont } from 'app/entities/enumerations/identificaciont.model';
import { Sexop } from 'app/entities/enumerations/sexop.model';

export interface IPaciente {
  id?: number;
  nombre?: string | null;
  tipoIdentificacion?: Identificaciont | null;
  identificacion?: number | null;
  edad?: number | null;
  sexo?: Sexop | null;
  pesoKG?: number | null;
  estaturaCM?: number | null;
  oximetriaReferencia?: number | null;
  temperaturaReferencia?: number | null;
  ritmoCardiacoReferencia?: number | null;
  presionSistolicaReferencia?: number | null;
  presionDistolicaReferencia?: number | null;
  comentarios?: string | null;
  pasosReferencia?: number | null;
  caloriasReferencia?: number | null;
  metaReferencia?: string | null;
  condicion?: ICondicion | null;
  ips?: IIPS | null;
  user?: IUser | null;
  tratamiento?: ITratamieto | null;
  farmaceutica?: IFarmaceutica | null;
}

export class Paciente implements IPaciente {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public tipoIdentificacion?: Identificaciont | null,
    public identificacion?: number | null,
    public edad?: number | null,
    public sexo?: Sexop | null,
    public pesoKG?: number | null,
    public estaturaCM?: number | null,
    public oximetriaReferencia?: number | null,
    public temperaturaReferencia?: number | null,
    public ritmoCardiacoReferencia?: number | null,
    public presionSistolicaReferencia?: number | null,
    public presionDistolicaReferencia?: number | null,
    public comentarios?: string | null,
    public pasosReferencia?: number | null,
    public caloriasReferencia?: number | null,
    public metaReferencia?: string | null,
    public condicion?: ICondicion | null,
    public ips?: IIPS | null,
    public user?: IUser | null,
    public tratamiento?: ITratamieto | null,
    public farmaceutica?: IFarmaceutica | null
  ) {}
}

export function getPacienteIdentifier(paciente: IPaciente): number | undefined {
  return paciente.id;
}
