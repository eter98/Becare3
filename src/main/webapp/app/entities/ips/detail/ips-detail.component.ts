import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIPS } from '../ips.model';

@Component({
  selector: 'jhi-ips-detail',
  templateUrl: './ips-detail.component.html',
})
export class IPSDetailComponent implements OnInit {
  iPS: IIPS | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ iPS }) => {
      this.iPS = iPS;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
