import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICompras, Compras } from '../compras.model';

import { ComprasService } from './compras.service';

describe('Compras Service', () => {
  let service: ComprasService;
  let httpMock: HttpTestingController;
  let elemDefault: ICompras;
  let expectedResult: ICompras | ICompras[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ComprasService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fechaCreacion: currentDate,
      valorCompra: 0,
      valorPagado: 0,
      valorDeuda: 0,
      estado: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Compras', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.create(new Compras()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Compras', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          valorCompra: 1,
          valorPagado: 1,
          valorDeuda: 1,
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Compras', () => {
      const patchObject = Object.assign(
        {
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          valorPagado: 1,
          estado: 'BBBBBB',
        },
        new Compras()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Compras', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          valorCompra: 1,
          valorPagado: 1,
          valorDeuda: 1,
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Compras', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addComprasToCollectionIfMissing', () => {
      it('should add a Compras to an empty array', () => {
        const compras: ICompras = { id: 123 };
        expectedResult = service.addComprasToCollectionIfMissing([], compras);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compras);
      });

      it('should not add a Compras to an array that contains it', () => {
        const compras: ICompras = { id: 123 };
        const comprasCollection: ICompras[] = [
          {
            ...compras,
          },
          { id: 456 },
        ];
        expectedResult = service.addComprasToCollectionIfMissing(comprasCollection, compras);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Compras to an array that doesn't contain it", () => {
        const compras: ICompras = { id: 123 };
        const comprasCollection: ICompras[] = [{ id: 456 }];
        expectedResult = service.addComprasToCollectionIfMissing(comprasCollection, compras);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compras);
      });

      it('should add only unique Compras to an array', () => {
        const comprasArray: ICompras[] = [{ id: 123 }, { id: 456 }, { id: 88200 }];
        const comprasCollection: ICompras[] = [{ id: 123 }];
        expectedResult = service.addComprasToCollectionIfMissing(comprasCollection, ...comprasArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const compras: ICompras = { id: 123 };
        const compras2: ICompras = { id: 456 };
        expectedResult = service.addComprasToCollectionIfMissing([], compras, compras2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compras);
        expect(expectedResult).toContain(compras2);
      });

      it('should accept null and undefined values', () => {
        const compras: ICompras = { id: 123 };
        expectedResult = service.addComprasToCollectionIfMissing([], null, compras, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compras);
      });

      it('should return initial array if no Compras is added', () => {
        const comprasCollection: ICompras[] = [{ id: 123 }];
        expectedResult = service.addComprasToCollectionIfMissing(comprasCollection, undefined, null);
        expect(expectedResult).toEqual(comprasCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
