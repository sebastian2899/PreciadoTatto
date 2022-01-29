jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICitaTatto, CitaTatto } from '../cita-tatto.model';
import { CitaTattoService } from '../service/cita-tatto.service';

import { CitaTattoRoutingResolveService } from './cita-tatto-routing-resolve.service';

describe('CitaTatto routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CitaTattoRoutingResolveService;
  let service: CitaTattoService;
  let resultCitaTatto: ICitaTatto | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CitaTattoRoutingResolveService);
    service = TestBed.inject(CitaTattoService);
    resultCitaTatto = undefined;
  });

  describe('resolve', () => {
    it('should return ICitaTatto returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCitaTatto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCitaTatto).toEqual({ id: 123 });
    });

    it('should return new ICitaTatto if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCitaTatto = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCitaTatto).toEqual(new CitaTatto());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CitaTatto })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCitaTatto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCitaTatto).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
