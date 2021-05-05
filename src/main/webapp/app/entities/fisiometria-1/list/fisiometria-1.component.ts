import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFisiometria1 } from '../fisiometria-1.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { Fisiometria1Service } from '../service/fisiometria-1.service';
import { Fisiometria1DeleteDialogComponent } from '../delete/fisiometria-1-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-fisiometria-1',
  templateUrl: './fisiometria-1.component.html',
})
export class Fisiometria1Component implements OnInit {
  fisiometria1s: IFisiometria1[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected fisiometria1Service: Fisiometria1Service, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.fisiometria1s = [];
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

    this.fisiometria1Service
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IFisiometria1[]>) => {
          this.isLoading = false;
          this.paginateFisiometria1s(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.fisiometria1s = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFisiometria1): number {
    return item.id!;
  }

  delete(fisiometria1: IFisiometria1): void {
    const modalRef = this.modalService.open(Fisiometria1DeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fisiometria1 = fisiometria1;
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

  protected paginateFisiometria1s(data: IFisiometria1[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.fisiometria1s.push(d);
      }
    }
  }
}
