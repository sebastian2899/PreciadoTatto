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
import { ProductoService } from 'app/entities/producto/service/producto.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  citasTatto?: ICitaTatto[] = [];
  citasPerfo?: ICitaPerforacion[] = [];
  totalProducts?: number | null;
  totalVenta?: number | null;
  totalCompra?: number | null;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private citaTattoService: CitaTattoService,
    private citaPerfoService: CitaPerforacionService,
    private productoService: ProductoService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    if (this.account) {
      this.consultarTattos();
      this.consultarPerfos();
      this.totalProductos();
      this.totalVentas();
      this.totalComp();
    }
  }

  refrescar(): void {
    this.consultarTattos();
    this.consultarPerfos();
    this.totalProductos();
    this.totalVentas();
    this.totalComp();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  totalProductos(): void {
    this.productoService.totalProducto().subscribe(
      (res: HttpResponse<number>) => {
        this.totalProducts = res.body;
      },
      () => {
        this.totalProducts = 0;
      }
    );
  }

  totalComp(): void {
    this.productoService.totalCompras().subscribe(
      (res: HttpResponse<number>) => {
        this.totalCompra = res.body;
      },
      () => {
        this.totalCompra = 0;
      }
    );
  }

  totalVentas(): void {
    this.productoService.totalVentas().subscribe(
      (res: HttpResponse<number>) => {
        this.totalVenta = res.body;
      },
      () => {
        this.totalVenta = 0;
      }
    );
  }

  consultarTattos(): void {
    this.citaTattoService.queryDay().subscribe(
      (res: HttpResponse<ICitaTatto[]>) => {
        this.citasTatto = res.body ?? [];
      },
      () => {
        this.citasTatto = [];
      }
    );
  }

  consultarPerfos(): void {
    this.citaPerfoService.queryDay().subscribe(
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
