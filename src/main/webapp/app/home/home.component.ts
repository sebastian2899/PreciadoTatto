import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { CitaTattoService } from 'app/entities/cita-tatto/service/cita-tatto.service';
import { CitaPerforacionService } from 'app/entities/cita-perforacion/service/cita-perforacion.service';
import { ICitaTatto } from 'app/entities/cita-tatto/cita-tatto.model';
import { HttpResponse } from '@angular/common/http';
import { ICitaPerforacion } from 'app/entities/cita-perforacion/cita-perforacion.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  citasTatto?: ICitaTatto[] = [];
  citasPerfo?: ICitaPerforacion[] = [];

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private citaTattoService: CitaTattoService,
    private citaPerfoService: CitaPerforacionService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    if (this.account) {
      this.consultarTattos();
      this.consultarPerfos();
    }
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  consultarTattos(): void {
    this.citaTattoService.query().subscribe(
      (res: HttpResponse<ICitaTatto[]>) => {
        this.citasTatto = res.body ?? [];
      },
      () => {
        this.citasTatto = [];
      }
    );
  }

  consultarPerfos(): void {
    this.citaPerfoService.query().subscribe(
      (res: HttpResponse<ICitaPerforacion[]>) => {
        this.citasPerfo = res.body ?? [];
      },
      () => {
        this.citasPerfo = [];
      }
    );
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
