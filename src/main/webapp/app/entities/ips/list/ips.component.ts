import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIPS } from '../ips.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { IPSService } from '../service/ips.service';
import { IPSDeleteDialogComponent } from '../delete/ips-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-ips',
  templateUrl: './ips.component.html',
})
export class IPSComponent implements OnInit {
  iPS: IIPS[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected iPSService: IPSService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.iPS = [];
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

    this.iPSService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IIPS[]>) => {
          this.isLoading = false;
          this.paginateIPS(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.iPS = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IIPS): number {
    return item.id!;
  }

  delete(iPS: IIPS): void {
    const modalRef = this.modalService.open(IPSDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.iPS = iPS;
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

  protected paginateIPS(data: IIPS[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.iPS.push(d);
      }
    }
  }
}
