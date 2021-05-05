import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPregunta } from '../pregunta.model';

@Component({
  selector: 'jhi-pregunta-detail',
  templateUrl: './pregunta-detail.component.html',
})
export class PreguntaDetailComponent implements OnInit {
  pregunta: IPregunta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pregunta }) => {
      this.pregunta = pregunta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
