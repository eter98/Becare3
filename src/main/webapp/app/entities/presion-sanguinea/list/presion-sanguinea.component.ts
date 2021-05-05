import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPresionSanguinea } from '../presion-sanguinea.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PresionSanguineaService } from '../service/presion-sanguinea.service';
import { PresionSanguineaDeleteDialogComponent } from '../delete/presion-sanguinea-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-presion-sanguinea',
  templateUrl: './presion-sanguinea.component.html',
})
export class PresionSanguineaComponent implements OnInit {
  presionSanguineas: IPresionSanguinea[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected presionSanguineaService: PresionSanguineaService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.presionSanguineas = [];
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

    this.presionSanguineaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IPresionSanguinea[]>) => {
          this.isLoading = false;
          this.paginatePresionSanguineas(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.presionSanguineas = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPresionSanguinea): number {
    return item.id!;
  }

  delete(presionSanguinea: IPresionSanguinea): void {
    const modalRef = this.modalService.open(PresionSanguineaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.presionSanguinea = presionSanguinea;
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

  protected paginatePresionSanguineas(data: IPresionSanguinea[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.presionSanguineas.push(d);
      }
    }
  }
}
