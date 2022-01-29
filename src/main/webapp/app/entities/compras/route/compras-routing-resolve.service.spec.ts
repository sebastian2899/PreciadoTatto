jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICompras, Compras } from '../compras.model';
import { ComprasService } from '../service/compras.service';

import { ComprasRoutingResolveService } from './compras-routing-resolve.service';

describe('Compras routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ComprasRoutingResolveService;
  let service: ComprasService;
  let resultCompras: ICompras | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ComprasRoutingResolveService);
    service = TestBed.inject(ComprasService);
    resultCompras = undefined;
  });

  describe('resolve', () => {
    it('should return ICompras returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompras = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCompras).toEqual({ id: 123 });
    });

    it('should return new ICompras if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompras = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCompras).toEqual(new Compras());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Compras })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompras = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCompras).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
