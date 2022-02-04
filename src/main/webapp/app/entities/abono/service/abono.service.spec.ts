import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAbono, Abono } from '../abono.model';

import { AbonoService } from './abono.service';

describe('Abono Service', () => {
  let service: AbonoService;
  let httpMock: HttpTestingController;
  let elemDefault: IAbono;
  let expectedResult: IAbono | IAbono[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AbonoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fechaAbono: currentDate,
      idFactura: 0,
      valorAbono: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaAbono: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Abono', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaAbono: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaAbono: currentDate,
        },
        returnedFromService
      );

      service.create(new Abono()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Abono', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaAbono: currentDate.format(DATE_TIME_FORMAT),
          idFactura: 1,
          valorAbono: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaAbono: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Abono', () => {
      const patchObject = Object.assign(
        {
          idFactura: 1,
        },
        new Abono()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaAbono: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Abono', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaAbono: currentDate.format(DATE_TIME_FORMAT),
          idFactura: 1,
          valorAbono: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaAbono: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Abono', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAbonoToCollectionIfMissing', () => {
      it('should add a Abono to an empty array', () => {
        const abono: IAbono = { id: 123 };
        expectedResult = service.addAbonoToCollectionIfMissing([], abono);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(abono);
      });

      it('should not add a Abono to an array that contains it', () => {
        const abono: IAbono = { id: 123 };
        const abonoCollection: IAbono[] = [
          {
            ...abono,
          },
          { id: 456 },
        ];
        expectedResult = service.addAbonoToCollectionIfMissing(abonoCollection, abono);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Abono to an array that doesn't contain it", () => {
        const abono: IAbono = { id: 123 };
        const abonoCollection: IAbono[] = [{ id: 456 }];
        expectedResult = service.addAbonoToCollectionIfMissing(abonoCollection, abono);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(abono);
      });

      it('should add only unique Abono to an array', () => {
        const abonoArray: IAbono[] = [{ id: 123 }, { id: 456 }, { id: 9215 }];
        const abonoCollection: IAbono[] = [{ id: 123 }];
        expectedResult = service.addAbonoToCollectionIfMissing(abonoCollection, ...abonoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const abono: IAbono = { id: 123 };
        const abono2: IAbono = { id: 456 };
        expectedResult = service.addAbonoToCollectionIfMissing([], abono, abono2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(abono);
        expect(expectedResult).toContain(abono2);
      });

      it('should accept null and undefined values', () => {
        const abono: IAbono = { id: 123 };
        expectedResult = service.addAbonoToCollectionIfMissing([], null, abono, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(abono);
      });

      it('should return initial array if no Abono is added', () => {
        const abonoCollection: IAbono[] = [{ id: 123 }];
        expectedResult = service.addAbonoToCollectionIfMissing(abonoCollection, undefined, null);
        expect(expectedResult).toEqual(abonoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
