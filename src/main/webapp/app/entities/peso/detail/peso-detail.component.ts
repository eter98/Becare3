import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPeso } from '../peso.model';

@Component({
  selector: 'jhi-peso-detail',
  templateUrl: './peso-detail.component.html',
})
export class PesoDetailComponent implements OnInit {
  peso: IPeso | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ peso }) => {
      this.peso = peso;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
