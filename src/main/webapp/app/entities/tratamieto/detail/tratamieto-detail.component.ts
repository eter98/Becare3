import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITratamieto } from '../tratamieto.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-tratamieto-detail',
  templateUrl: './tratamieto-detail.component.html',
})
export class TratamietoDetailComponent implements OnInit {
  tratamieto: ITratamieto | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tratamieto }) => {
      this.tratamieto = tratamieto;
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
