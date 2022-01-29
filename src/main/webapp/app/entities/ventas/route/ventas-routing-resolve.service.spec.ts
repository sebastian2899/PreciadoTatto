jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVentas, Ventas } from '../ventas.model';
import { VentasService } from '../service/ventas.service';

import { VentasRoutingResolveService } from './ventas-routing-resolve.service';

describe('Ventas routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VentasRoutingResolveService;
  let service: VentasService;
  let resultVentas: IVentas | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(VentasRoutingResolveService);
    service = TestBed.inject(VentasService);
    resultVentas = undefined;
  });

  describe('resolve', () => {
    it('should return IVentas returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVentas = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVentas).toEqual({ id: 123 });
    });

    it('should return new IVentas if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVentas = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVentas).toEqual(new Ventas());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Ventas })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVentas = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVentas).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
