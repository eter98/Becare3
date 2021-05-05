import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdherencia } from '../adherencia.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AdherenciaService } from '../service/adherencia.service';
import { AdherenciaDeleteDialogComponent } from '../delete/adherencia-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-adherencia',
  templateUrl: './adherencia.component.html',
})
export class AdherenciaComponent implements OnInit {
  adherencias: IAdherencia[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected adherenciaService: AdherenciaService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.adherencias = [];
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

    this.adherenciaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IAdherencia[]>) => {
          this.isLoading = false;
          this.paginateAdherencias(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.adherencias = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAdherencia): number {
    return item.id!;
  }

  delete(adherencia: IAdherencia): void {
    const modalRef = this.modalService.open(AdherenciaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.adherencia = adherencia;
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

  protected paginateAdherencias(data: IAdherencia[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.adherencias.push(d);
      }
    }
  }
}
