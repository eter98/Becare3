import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDispositivo } from '../dispositivo.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DispositivoService } from '../service/dispositivo.service';
import { DispositivoDeleteDialogComponent } from '../delete/dispositivo-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-dispositivo',
  templateUrl: './dispositivo.component.html',
})
export class DispositivoComponent implements OnInit {
  dispositivos: IDispositivo[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected dispositivoService: DispositivoService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.dispositivos = [];
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

    this.dispositivoService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IDispositivo[]>) => {
          this.isLoading = false;
          this.paginateDispositivos(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.dispositivos = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDispositivo): number {
    return item.id!;
  }

  delete(dispositivo: IDispositivo): void {
    const modalRef = this.modalService.open(DispositivoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dispositivo = dispositivo;
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

  protected paginateDispositivos(data: IDispositivo[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.dispositivos.push(d);
      }
    }
  }
}
