import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMedicamento } from '../medicamento.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-medicamento-detail',
  templateUrl: './medicamento-detail.component.html',
})
export class MedicamentoDetailComponent implements OnInit {
  medicamento: IMedicamento | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicamento }) => {
      this.medicamento = medicamento;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
