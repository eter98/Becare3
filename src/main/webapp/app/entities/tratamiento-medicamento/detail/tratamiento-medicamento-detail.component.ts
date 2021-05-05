import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITratamientoMedicamento } from '../tratamiento-medicamento.model';

@Component({
  selector: 'jhi-tratamiento-medicamento-detail',
  templateUrl: './tratamiento-medicamento-detail.component.html',
})
export class TratamientoMedicamentoDetailComponent implements OnInit {
  tratamientoMedicamento: ITratamientoMedicamento | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tratamientoMedicamento }) => {
      this.tratamientoMedicamento = tratamientoMedicamento;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
