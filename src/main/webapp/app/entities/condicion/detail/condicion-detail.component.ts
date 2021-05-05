import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICondicion } from '../condicion.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-condicion-detail',
  templateUrl: './condicion-detail.component.html',
})
export class CondicionDetailComponent implements OnInit {
  condicion: ICondicion | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ condicion }) => {
      this.condicion = condicion;
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
