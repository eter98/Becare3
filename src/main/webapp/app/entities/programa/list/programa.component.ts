import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrograma } from '../programa.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ProgramaService } from '../service/programa.service';
import { ProgramaDeleteDialogComponent } from '../delete/programa-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-programa',
  templateUrl: './programa.component.html',
})
export class ProgramaComponent implements OnInit {
  programas: IPrograma[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected programaService: ProgramaService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.programas = [];
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

    this.programaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IPrograma[]>) => {
          this.isLoading = false;
          this.paginateProgramas(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.programas = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPrograma): number {
    return item.id!;
  }

  delete(programa: IPrograma): void {
    const modalRef = this.modalService.open(ProgramaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.programa = programa;
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

  protected paginateProgramas(data: IPrograma[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.programas.push(d);
      }
    }
  }
}
