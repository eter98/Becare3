import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPresionSanguinea } from '../presion-sanguinea.model';

@Component({
  selector: 'jhi-presion-sanguinea-detail',
  templateUrl: './presion-sanguinea-detail.component.html',
})
export class PresionSanguineaDetailComponent implements OnInit {
  presionSanguinea: IPresionSanguinea | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ presionSanguinea }) => {
      this.presionSanguinea = presionSanguinea;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
