import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICuestionarioEstado } from '../cuestionario-estado.model';

@Component({
  selector: 'jhi-cuestionario-estado-detail',
  templateUrl: './cuestionario-estado-detail.component.html',
})
export class CuestionarioEstadoDetailComponent implements OnInit {
  cuestionarioEstado: ICuestionarioEstado | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cuestionarioEstado }) => {
      this.cuestionarioEstado = cuestionarioEstado;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
