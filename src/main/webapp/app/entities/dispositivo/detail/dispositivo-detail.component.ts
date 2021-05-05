import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDispositivo } from '../dispositivo.model';

@Component({
  selector: 'jhi-dispositivo-detail',
  templateUrl: './dispositivo-detail.component.html',
})
export class DispositivoDetailComponent implements OnInit {
  dispositivo: IDispositivo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dispositivo }) => {
      this.dispositivo = dispositivo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
