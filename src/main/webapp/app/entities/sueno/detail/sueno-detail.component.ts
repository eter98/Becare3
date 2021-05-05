import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISueno } from '../sueno.model';

@Component({
  selector: 'jhi-sueno-detail',
  templateUrl: './sueno-detail.component.html',
})
export class SuenoDetailComponent implements OnInit {
  sueno: ISueno | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sueno }) => {
      this.sueno = sueno;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
