import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPasos } from '../pasos.model';

@Component({
  selector: 'jhi-pasos-detail',
  templateUrl: './pasos-detail.component.html',
})
export class PasosDetailComponent implements OnInit {
  pasos: IPasos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pasos }) => {
      this.pasos = pasos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
