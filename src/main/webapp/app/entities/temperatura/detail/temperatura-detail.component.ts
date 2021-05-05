import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITemperatura } from '../temperatura.model';

@Component({
  selector: 'jhi-temperatura-detail',
  templateUrl: './temperatura-detail.component.html',
})
export class TemperaturaDetailComponent implements OnInit {
  temperatura: ITemperatura | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ temperatura }) => {
      this.temperatura = temperatura;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
