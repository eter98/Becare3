import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITokenDisp } from '../token-disp.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { TokenDispService } from '../service/token-disp.service';
import { TokenDispDeleteDialogComponent } from '../delete/token-disp-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-token-disp',
  templateUrl: './token-disp.component.html',
})
export class TokenDispComponent implements OnInit {
  tokenDisps: ITokenDisp[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected tokenDispService: TokenDispService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.tokenDisps = [];
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

    this.tokenDispService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ITokenDisp[]>) => {
          this.isLoading = false;
          this.paginateTokenDisps(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.tokenDisps = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITokenDisp): number {
    return item.id!;
  }

  delete(tokenDisp: ITokenDisp): void {
    const modalRef = this.modalService.open(TokenDispDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tokenDisp = tokenDisp;
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

  protected paginateTokenDisps(data: ITokenDisp[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.tokenDisps.push(d);
      }
    }
  }
}
