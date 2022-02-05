import { Injectable } from '@angular/core';
import { SessionStorageService } from 'ngx-webstorage';

@Injectable({ providedIn: 'root' })
export class StateStorageService {
  private previousUrlKey = 'previousUrl';

  private citaKey = 'citaKey';

  private idClientecitaKey = 'idClienteCita';

  constructor(private sessionStorageService: SessionStorageService) {}

  storeUrl(url: string): void {
    this.sessionStorageService.store(this.previousUrlKey, url);
  }

  getUrl(): string | null {
    return this.sessionStorageService.retrieve(this.previousUrlKey) as string | null;
  }

  pasoParametroCita(idCita: number): void {
    this.sessionStorageService.store(this.citaKey, idCita);
  }

  getParametroCita(): number | null {
    return this.sessionStorageService.retrieve(this.citaKey) as number | null;
  }

  clearUrl(): void {
    this.sessionStorageService.clear(this.previousUrlKey);
  }
}
