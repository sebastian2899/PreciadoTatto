import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICajaTattos } from '../caja-tattos.model';
import { CajaTattosService } from '../service/caja-tattos.service';
import { CajaTattosDeleteDialogComponent } from '../delete/caja-tattos-delete-dialog.component';
import { RouteConfigLoadEnd, Router } from '@angular/router';

@Component({
  selector: 'jhi-caja-tattos',
  templateUrl: './caja-tattos.component.html',
})
export class CajaTattosComponent implements OnInit {
  cajaTattos?: ICajaTattos[];
  isLoading = false;
  validCaja = true;

  constructor(protected router: Router, protected cajaTattosService: CajaTattosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.cajaTattosService.query().subscribe(
      (res: HttpResponse<ICajaTattos[]>) => {
        this.isLoading = false;
        this.cajaTattos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICajaTattos): number {
    return item.id!;
  }

  consultarCajaFecha(): void {
    this.router.navigate(['/caja-tattos/caja-fechas']);
  }

  delete(cajaTattos: ICajaTattos): void {
    const modalRef = this.modalService.open(CajaTattosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cajaTattos = cajaTattos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
