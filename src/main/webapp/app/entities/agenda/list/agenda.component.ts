import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAgenda } from '../agenda.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AgendaService } from '../service/agenda.service';
import { AgendaDeleteDialogComponent } from '../delete/agenda-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-agenda',
  templateUrl: './agenda.component.html',
})
export class AgendaComponent implements OnInit {
  agenda: IAgenda[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected agendaService: AgendaService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.agenda = [];
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

    this.agendaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IAgenda[]>) => {
          this.isLoading = false;
          this.paginateAgenda(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.agenda = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAgenda): number {
    return item.id!;
  }

  delete(agenda: IAgenda): void {
    const modalRef = this.modalService.open(AgendaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.agenda = agenda;
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

  protected paginateAgenda(data: IAgenda[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.agenda.push(d);
      }
    }
  }
}
