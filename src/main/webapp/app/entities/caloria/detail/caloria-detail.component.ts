import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICaloria } from '../caloria.model';

@Component({
  selector: 'jhi-caloria-detail',
  templateUrl: './caloria-detail.component.html',
})
export class CaloriaDetailComponent implements OnInit {
  caloria: ICaloria | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ caloria }) => {
      this.caloria = caloria;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
