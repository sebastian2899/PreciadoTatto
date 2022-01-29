import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICajaIngresos, CajaIngresos } from '../caja-ingresos.model';

import { CajaIngresosService } from './caja-ingresos.service';

describe('CajaIngresos Service', () => {
  let service: CajaIngresosService;
  let httpMock: HttpTestingController;
  let elemDefault: ICajaIngresos;
  let expectedResult: ICajaIngresos | ICajaIngresos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CajaIngresosService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      valorVendidoDia: 0,
      valorRegistradoDia: 0,
      diferencia: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CajaIngresos', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CajaIngresos()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CajaIngresos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          valorVendidoDia: 1,
          valorRegistradoDia: 1,
          diferencia: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CajaIngresos', () => {
      const patchObject = Object.assign(
        {
          diferencia: 1,
        },
        new CajaIngresos()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CajaIngresos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          valorVendidoDia: 1,
          valorRegistradoDia: 1,
          diferencia: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CajaIngresos', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCajaIngresosToCollectionIfMissing', () => {
      it('should add a CajaIngresos to an empty array', () => {
        const cajaIngresos: ICajaIngresos = { id: 123 };
        expectedResult = service.addCajaIngresosToCollectionIfMissing([], cajaIngresos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cajaIngresos);
      });

      it('should not add a CajaIngresos to an array that contains it', () => {
        const cajaIngresos: ICajaIngresos = { id: 123 };
        const cajaIngresosCollection: ICajaIngresos[] = [
          {
            ...cajaIngresos,
          },
          { id: 456 },
        ];
        expectedResult = service.addCajaIngresosToCollectionIfMissing(cajaIngresosCollection, cajaIngresos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CajaIngresos to an array that doesn't contain it", () => {
        const cajaIngresos: ICajaIngresos = { id: 123 };
        const cajaIngresosCollection: ICajaIngresos[] = [{ id: 456 }];
        expectedResult = service.addCajaIngresosToCollectionIfMissing(cajaIngresosCollection, cajaIngresos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cajaIngresos);
      });

      it('should add only unique CajaIngresos to an array', () => {
        const cajaIngresosArray: ICajaIngresos[] = [{ id: 123 }, { id: 456 }, { id: 70629 }];
        const cajaIngresosCollection: ICajaIngresos[] = [{ id: 123 }];
        expectedResult = service.addCajaIngresosToCollectionIfMissing(cajaIngresosCollection, ...cajaIngresosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cajaIngresos: ICajaIngresos = { id: 123 };
        const cajaIngresos2: ICajaIngresos = { id: 456 };
        expectedResult = service.addCajaIngresosToCollectionIfMissing([], cajaIngresos, cajaIngresos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cajaIngresos);
        expect(expectedResult).toContain(cajaIngresos2);
      });

      it('should accept null and undefined values', () => {
        const cajaIngresos: ICajaIngresos = { id: 123 };
        expectedResult = service.addCajaIngresosToCollectionIfMissing([], null, cajaIngresos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cajaIngresos);
      });

      it('should return initial array if no CajaIngresos is added', () => {
        const cajaIngresosCollection: ICajaIngresos[] = [{ id: 123 }];
        expectedResult = service.addCajaIngresosToCollectionIfMissing(cajaIngresosCollection, undefined, null);
        expect(expectedResult).toEqual(cajaIngresosCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
