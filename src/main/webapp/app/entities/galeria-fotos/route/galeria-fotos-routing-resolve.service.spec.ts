jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IGaleriaFotos, GaleriaFotos } from '../galeria-fotos.model';
import { GaleriaFotosService } from '../service/galeria-fotos.service';

import { GaleriaFotosRoutingResolveService } from './galeria-fotos-routing-resolve.service';

describe('GaleriaFotos routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: GaleriaFotosRoutingResolveService;
  let service: GaleriaFotosService;
  let resultGaleriaFotos: IGaleriaFotos | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(GaleriaFotosRoutingResolveService);
    service = TestBed.inject(GaleriaFotosService);
    resultGaleriaFotos = undefined;
  });

  describe('resolve', () => {
    it('should return IGaleriaFotos returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGaleriaFotos = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGaleriaFotos).toEqual({ id: 123 });
    });

    it('should return new IGaleriaFotos if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGaleriaFotos = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultGaleriaFotos).toEqual(new GaleriaFotos());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as GaleriaFotos })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGaleriaFotos = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGaleriaFotos).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
