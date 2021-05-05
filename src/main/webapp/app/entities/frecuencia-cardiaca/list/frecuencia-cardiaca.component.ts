import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFrecuenciaCardiaca } from '../frecuencia-cardiaca.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { FrecuenciaCardiacaService } from '../service/frecuencia-cardiaca.service';
import { FrecuenciaCardiacaDeleteDialogComponent } from '../delete/frecuencia-cardiaca-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-frecuencia-cardiaca',
  templateUrl: './frecuencia-cardiaca.component.html',
})
export class FrecuenciaCardiacaComponent implements OnInit {
  frecuenciaCardiacas: IFrecuenciaCardiaca[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected frecuenciaCardiacaService: FrecuenciaCardiacaService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.frecuenciaCardiacas = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.frecuenciaCardiacaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IFrecuenciaCardiaca[]>) => {
          this.isLoading = false;
          this.paginateFrecuenciaCardiacas(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.frecuenciaCardiacas = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFrecuenciaCardiaca): number {
    return item.id!;
  }

  delete(frecuenciaCardiaca: IFrecuenciaCardiaca): void {
    const modalRef = this.modalService.open(FrecuenciaCardiacaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.frecuenciaCardiaca = frecuenciaCardiaca;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFrecuenciaCardiacas(data: IFrecuenciaCardiaca[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.frecuenciaCardiacas.push(d);
      }
    }
  }
}
