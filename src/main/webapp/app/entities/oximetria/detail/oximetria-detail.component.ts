import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOximetria } from '../oximetria.model';

@Component({
  selector: 'jhi-oximetria-detail',
  templateUrl: './oximetria-detail.component.html',
})
export class OximetriaDetailComponent implements OnInit {
  oximetria: IOximetria | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ oximetria }) => {
      this.oximetria = oximetria;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
