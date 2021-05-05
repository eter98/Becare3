import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPregunta } from '../pregunta.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PreguntaService } from '../service/pregunta.service';
import { PreguntaDeleteDialogComponent } from '../delete/pregunta-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-pregunta',
  templateUrl: './pregunta.component.html',
})
export class PreguntaComponent implements OnInit {
  preguntas: IPregunta[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected preguntaService: PreguntaService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.preguntas = [];
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

    this.preguntaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IPregunta[]>) => {
          this.isLoading = false;
          this.paginatePreguntas(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.preguntas = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPregunta): number {
    return item.id!;
  }

  delete(pregunta: IPregunta): void {
    const modalRef = this.modalService.open(PreguntaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pregunta = pregunta;
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

  protected paginatePreguntas(data: IPregunta[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.preguntas.push(d);
      }
    }
  }
}
