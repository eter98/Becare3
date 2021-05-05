import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFarmaceutica } from '../farmaceutica.model';

@Component({
  selector: 'jhi-farmaceutica-detail',
  templateUrl: './farmaceutica-detail.component.html',
})
export class FarmaceuticaDetailComponent implements OnInit {
  farmaceutica: IFarmaceutica | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ farmaceutica }) => {
      this.farmaceutica = farmaceutica;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
