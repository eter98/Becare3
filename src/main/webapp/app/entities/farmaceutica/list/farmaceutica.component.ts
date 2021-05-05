import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFarmaceutica } from '../farmaceutica.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { FarmaceuticaService } from '../service/farmaceutica.service';
import { FarmaceuticaDeleteDialogComponent } from '../delete/farmaceutica-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-farmaceutica',
  templateUrl: './farmaceutica.component.html',
})
export class FarmaceuticaComponent implements OnInit {
  farmaceuticas: IFarmaceutica[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected farmaceuticaService: FarmaceuticaService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.farmaceuticas = [];
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

    this.farmaceuticaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IFarmaceutica[]>) => {
          this.isLoading = false;
          this.paginateFarmaceuticas(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.farmaceuticas = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFarmaceutica): number {
    return item.id!;
  }

  delete(farmaceutica: IFarmaceutica): void {
    const modalRef = this.modalService.open(FarmaceuticaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.farmaceutica = farmaceutica;
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

  protected paginateFarmaceuticas(data: IFarmaceutica[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.farmaceuticas.push(d);
      }
    }
  }
}
