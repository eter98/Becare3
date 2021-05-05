import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAlarma } from '../alarma.model';

@Component({
  selector: 'jhi-alarma-detail',
  templateUrl: './alarma-detail.component.html',
})
export class AlarmaDetailComponent implements OnInit {
  alarma: IAlarma | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alarma }) => {
      this.alarma = alarma;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
