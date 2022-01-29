jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICajaIngresos, CajaIngresos } from '../caja-ingresos.model';
import { CajaIngresosService } from '../service/caja-ingresos.service';

import { CajaIngresosRoutingResolveService } from './caja-ingresos-routing-resolve.service';

describe('CajaIngresos routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CajaIngresosRoutingResolveService;
  let service: CajaIngresosService;
  let resultCajaIngresos: ICajaIngresos | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CajaIngresosRoutingResolveService);
    service = TestBed.inject(CajaIngresosService);
    resultCajaIngresos = undefined;
  });

  describe('resolve', () => {
    it('should return ICajaIngresos returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCajaIngresos = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCajaIngresos).toEqual({ id: 123 });
    });

    it('should return new ICajaIngresos if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCajaIngresos = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCajaIngresos).toEqual(new CajaIngresos());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CajaIngresos })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCajaIngresos = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCajaIngresos).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
