import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFisiometria1 } from '../fisiometria-1.model';

@Component({
  selector: 'jhi-fisiometria-1-detail',
  templateUrl: './fisiometria-1-detail.component.html',
})
export class Fisiometria1DetailComponent implements OnInit {
  fisiometria1: IFisiometria1 | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fisiometria1 }) => {
      this.fisiometria1 = fisiometria1;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
