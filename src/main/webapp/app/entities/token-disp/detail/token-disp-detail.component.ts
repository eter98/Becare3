import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITokenDisp } from '../token-disp.model';

@Component({
  selector: 'jhi-token-disp-detail',
  templateUrl: './token-disp-detail.component.html',
})
export class TokenDispDetailComponent implements OnInit {
  tokenDisp: ITokenDisp | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tokenDisp }) => {
      this.tokenDisp = tokenDisp;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
