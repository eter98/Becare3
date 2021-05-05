import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIngesta } from '../ingesta.model';

@Component({
  selector: 'jhi-ingesta-detail',
  templateUrl: './ingesta-detail.component.html',
})
export class IngestaDetailComponent implements OnInit {
  ingesta: IIngesta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ingesta }) => {
      this.ingesta = ingesta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
