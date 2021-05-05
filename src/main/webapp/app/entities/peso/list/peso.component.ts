import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPeso } from '../peso.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PesoService } from '../service/peso.service';
import { PesoDeleteDialogComponent } from '../delete/peso-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-peso',
  templateUrl: './peso.component.html',
})
export class PesoComponent implements OnInit {
  pesos: IPeso[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected pesoService: PesoService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.pesos = [];
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

    this.pesoService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IPeso[]>) => {
          this.isLoading = false;
          this.paginatePesos(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.pesos = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPeso): number {
    return item.id!;
  }

  delete(peso: IPeso): void {
    const modalRef = this.modalService.open(PesoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.peso = peso;
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

  protected paginatePesos(data: IPeso[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.pesos.push(d);
      }
    }
  }
}
