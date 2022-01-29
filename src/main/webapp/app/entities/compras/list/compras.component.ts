import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompras } from '../compras.model';
import { ComprasService } from '../service/compras.service';
import { ComprasDeleteDialogComponent } from '../delete/compras-delete-dialog.component';

@Component({
  selector: 'jhi-compras',
  templateUrl: './compras.component.html',
})
export class ComprasComponent implements OnInit {
  compras?: ICompras[];
  isLoading = false;

  constructor(protected comprasService: ComprasService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.comprasService.query().subscribe(
      (res: HttpResponse<ICompras[]>) => {
        this.isLoading = false;
        this.compras = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICompras): number {
    return item.id!;
  }

  delete(compras: ICompras): void {
    const modalRef = this.modalService.open(ComprasDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.compras = compras;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
