import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISueno } from '../sueno.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { SuenoService } from '../service/sueno.service';
import { SuenoDeleteDialogComponent } from '../delete/sueno-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-sueno',
  templateUrl: './sueno.component.html',
})
export class SuenoComponent implements OnInit {
  suenos: ISueno[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected suenoService: SuenoService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.suenos = [];
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

    this.suenoService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ISueno[]>) => {
          this.isLoading = false;
          this.paginateSuenos(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.suenos = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISueno): number {
    return item.id!;
  }

  delete(sueno: ISueno): void {
    const modalRef = this.modalService.open(SuenoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sueno = sueno;
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

  protected paginateSuenos(data: ISueno[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.suenos.push(d);
      }
    }
  }
}
