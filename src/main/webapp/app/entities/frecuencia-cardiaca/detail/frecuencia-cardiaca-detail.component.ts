import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFrecuenciaCardiaca } from '../frecuencia-cardiaca.model';

@Component({
  selector: 'jhi-frecuencia-cardiaca-detail',
  templateUrl: './frecuencia-cardiaca-detail.component.html',
})
export class FrecuenciaCardiacaDetailComponent implements OnInit {
  frecuenciaCardiaca: IFrecuenciaCardiaca | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ frecuenciaCardiaca }) => {
      this.frecuenciaCardiaca = frecuenciaCardiaca;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
