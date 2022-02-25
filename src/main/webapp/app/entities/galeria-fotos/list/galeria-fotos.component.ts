import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGaleriaFotos } from '../galeria-fotos.model';
import { GaleriaFotosService } from '../service/galeria-fotos.service';
import { GaleriaFotosDeleteDialogComponent } from '../delete/galeria-fotos-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-galeria-fotos',
  templateUrl: './galeria-fotos.component.html',
})
export class GaleriaFotosComponent implements OnInit {
  galeriaFotos?: IGaleriaFotos[];
  isLoading = false;
  pA = 1;

  constructor(protected galeriaFotosService: GaleriaFotosService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.galeriaFotosService.query().subscribe(
      (res: HttpResponse<IGaleriaFotos[]>) => {
        this.isLoading = false;
        this.galeriaFotos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IGaleriaFotos): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(galeriaFotos: IGaleriaFotos): void {
    const modalRef = this.modalService.open(GaleriaFotosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.galeriaFotos = galeriaFotos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
