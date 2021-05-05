import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrograma } from '../programa.model';

@Component({
  selector: 'jhi-programa-detail',
  templateUrl: './programa-detail.component.html',
})
export class ProgramaDetailComponent implements OnInit {
  programa: IPrograma | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ programa }) => {
      this.programa = programa;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
