import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAlarma } from '../alarma.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AlarmaService } from '../service/alarma.service';
import { AlarmaDeleteDialogComponent } from '../delete/alarma-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-alarma',
  templateUrl: './alarma.component.html',
})
export class AlarmaComponent implements OnInit {
  alarmas: IAlarma[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected alarmaService: AlarmaService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.alarmas = [];
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

    this.alarmaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IAlarma[]>) => {
          this.isLoading = false;
          this.paginateAlarmas(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.alarmas = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAlarma): number {
    return item.id!;
  }

  delete(alarma: IAlarma): void {
    const modalRef = this.modalService.open(AlarmaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.alarma = alarma;
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

  protected paginateAlarmas(data: IAlarma[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.alarmas.push(d);
      }
    }
  }
}
