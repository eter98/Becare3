import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIngesta } from '../ingesta.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { IngestaService } from '../service/ingesta.service';
import { IngestaDeleteDialogComponent } from '../delete/ingesta-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-ingesta',
  templateUrl: './ingesta.component.html',
})
export class IngestaComponent implements OnInit {
  ingestas: IIngesta[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected ingestaService: IngestaService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.ingestas = [];
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

    this.ingestaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IIngesta[]>) => {
          this.isLoading = false;
          this.paginateIngestas(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.ingestas = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IIngesta): number {
    return item.id!;
  }

  delete(ingesta: IIngesta): void {
    const modalRef = this.modalService.open(IngestaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ingesta = ingesta;
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

  protected paginateIngestas(data: IIngesta[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.ingestas.push(d);
      }
    }
  }
}
