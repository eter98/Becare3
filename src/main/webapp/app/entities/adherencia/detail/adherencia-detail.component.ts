import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAdherencia } from '../adherencia.model';

@Component({
  selector: 'jhi-adherencia-detail',
  templateUrl: './adherencia-detail.component.html',
})
export class AdherenciaDetailComponent implements OnInit {
  adherencia: IAdherencia | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adherencia }) => {
      this.adherencia = adherencia;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
