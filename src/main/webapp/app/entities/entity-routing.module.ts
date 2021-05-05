import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'adherencia',
        data: { pageTitle: 'becare3App.adherencia.home.title' },
        loadChildren: () => import('./adherencia/adherencia.module').then(m => m.AdherenciaModule),
      },
      {
        path: 'agenda',
        data: { pageTitle: 'becare3App.agenda.home.title' },
        loadChildren: () => import('./agenda/agenda.module').then(m => m.AgendaModule),
      },
      {
        path: 'alarma',
        data: { pageTitle: 'becare3App.alarma.home.title' },
        loadChildren: () => import('./alarma/alarma.module').then(m => m.AlarmaModule),
      },
      {
        path: 'caloria',
        data: { pageTitle: 'becare3App.caloria.home.title' },
        loadChildren: () => import('./caloria/caloria.module').then(m => m.CaloriaModule),
      },
      {
        path: 'condicion',
        data: { pageTitle: 'becare3App.condicion.home.title' },
        loadChildren: () => import('./condicion/condicion.module').then(m => m.CondicionModule),
      },
      {
        path: 'cuestionario-estado',
        data: { pageTitle: 'becare3App.cuestionarioEstado.home.title' },
        loadChildren: () => import('./cuestionario-estado/cuestionario-estado.module').then(m => m.CuestionarioEstadoModule),
      },
      {
        path: 'dispositivo',
        data: { pageTitle: 'becare3App.dispositivo.home.title' },
        loadChildren: () => import('./dispositivo/dispositivo.module').then(m => m.DispositivoModule),
      },
      {
        path: 'encuesta',
        data: { pageTitle: 'becare3App.encuesta.home.title' },
        loadChildren: () => import('./encuesta/encuesta.module').then(m => m.EncuestaModule),
      },
      {
        path: 'farmaceutica',
        data: { pageTitle: 'becare3App.farmaceutica.home.title' },
        loadChildren: () => import('./farmaceutica/farmaceutica.module').then(m => m.FarmaceuticaModule),
      },
      {
        path: 'fisiometria-1',
        data: { pageTitle: 'becare3App.fisiometria1.home.title' },
        loadChildren: () => import('./fisiometria-1/fisiometria-1.module').then(m => m.Fisiometria1Module),
      },
      {
        path: 'frecuencia-cardiaca',
        data: { pageTitle: 'becare3App.frecuenciaCardiaca.home.title' },
        loadChildren: () => import('./frecuencia-cardiaca/frecuencia-cardiaca.module').then(m => m.FrecuenciaCardiacaModule),
      },
      {
        path: 'ingesta',
        data: { pageTitle: 'becare3App.ingesta.home.title' },
        loadChildren: () => import('./ingesta/ingesta.module').then(m => m.IngestaModule),
      },
      {
        path: 'ips',
        data: { pageTitle: 'becare3App.iPS.home.title' },
        loadChildren: () => import('./ips/ips.module').then(m => m.IPSModule),
      },
      {
        path: 'medicamento',
        data: { pageTitle: 'becare3App.medicamento.home.title' },
        loadChildren: () => import('./medicamento/medicamento.module').then(m => m.MedicamentoModule),
      },
      {
        path: 'notificacion',
        data: { pageTitle: 'becare3App.notificacion.home.title' },
        loadChildren: () => import('./notificacion/notificacion.module').then(m => m.NotificacionModule),
      },
      {
        path: 'oximetria',
        data: { pageTitle: 'becare3App.oximetria.home.title' },
        loadChildren: () => import('./oximetria/oximetria.module').then(m => m.OximetriaModule),
      },
      {
        path: 'paciente',
        data: { pageTitle: 'becare3App.paciente.home.title' },
        loadChildren: () => import('./paciente/paciente.module').then(m => m.PacienteModule),
      },
      {
        path: 'pasos',
        data: { pageTitle: 'becare3App.pasos.home.title' },
        loadChildren: () => import('./pasos/pasos.module').then(m => m.PasosModule),
      },
      {
        path: 'peso',
        data: { pageTitle: 'becare3App.peso.home.title' },
        loadChildren: () => import('./peso/peso.module').then(m => m.PesoModule),
      },
      {
        path: 'pregunta',
        data: { pageTitle: 'becare3App.pregunta.home.title' },
        loadChildren: () => import('./pregunta/pregunta.module').then(m => m.PreguntaModule),
      },
      {
        path: 'presion-sanguinea',
        data: { pageTitle: 'becare3App.presionSanguinea.home.title' },
        loadChildren: () => import('./presion-sanguinea/presion-sanguinea.module').then(m => m.PresionSanguineaModule),
      },
      {
        path: 'programa',
        data: { pageTitle: 'becare3App.programa.home.title' },
        loadChildren: () => import('./programa/programa.module').then(m => m.ProgramaModule),
      },
      {
        path: 'sueno',
        data: { pageTitle: 'becare3App.sueno.home.title' },
        loadChildren: () => import('./sueno/sueno.module').then(m => m.SuenoModule),
      },
      {
        path: 'temperatura',
        data: { pageTitle: 'becare3App.temperatura.home.title' },
        loadChildren: () => import('./temperatura/temperatura.module').then(m => m.TemperaturaModule),
      },
      {
        path: 'token-disp',
        data: { pageTitle: 'becare3App.tokenDisp.home.title' },
        loadChildren: () => import('./token-disp/token-disp.module').then(m => m.TokenDispModule),
      },
      {
        path: 'tratamiento-medicamento',
        data: { pageTitle: 'becare3App.tratamientoMedicamento.home.title' },
        loadChildren: () => import('./tratamiento-medicamento/tratamiento-medicamento.module').then(m => m.TratamientoMedicamentoModule),
      },
      {
        path: 'tratamieto',
        data: { pageTitle: 'becare3App.tratamieto.home.title' },
        loadChildren: () => import('./tratamieto/tratamieto.module').then(m => m.TratamietoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
