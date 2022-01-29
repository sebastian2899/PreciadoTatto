import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICajaTattos, CajaTattos } from '../caja-tattos.model';

import { CajaTattosService } from './caja-tattos.service';

describe('CajaTattos Service', () => {
  let service: CajaTattosService;
  let httpMock: HttpTestingController;
  let elemDefault: ICajaTattos;
  let expectedResult: ICajaTattos | ICajaTattos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CajaTattosService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      valorTattoDia: 0,
      valorRegistrado: 0,
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

    it('should create a CajaTattos', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CajaTattos()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CajaTattos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          valorTattoDia: 1,
          valorRegistrado: 1,
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

    it('should partial update a CajaTattos', () => {
      const patchObject = Object.assign(
        {
          valorTattoDia: 1,
          valorRegistrado: 1,
        },
        new CajaTattos()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CajaTattos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          valorTattoDia: 1,
          valorRegistrado: 1,
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

    it('should delete a CajaTattos', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCajaTattosToCollectionIfMissing', () => {
      it('should add a CajaTattos to an empty array', () => {
        const cajaTattos: ICajaTattos = { id: 123 };
        expectedResult = service.addCajaTattosToCollectionIfMissing([], cajaTattos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cajaTattos);
      });

      it('should not add a CajaTattos to an array that contains it', () => {
        const cajaTattos: ICajaTattos = { id: 123 };
        const cajaTattosCollection: ICajaTattos[] = [
          {
            ...cajaTattos,
          },
          { id: 456 },
        ];
        expectedResult = service.addCajaTattosToCollectionIfMissing(cajaTattosCollection, cajaTattos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CajaTattos to an array that doesn't contain it", () => {
        const cajaTattos: ICajaTattos = { id: 123 };
        const cajaTattosCollection: ICajaTattos[] = [{ id: 456 }];
        expectedResult = service.addCajaTattosToCollectionIfMissing(cajaTattosCollection, cajaTattos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cajaTattos);
      });

      it('should add only unique CajaTattos to an array', () => {
        const cajaTattosArray: ICajaTattos[] = [{ id: 123 }, { id: 456 }, { id: 5749 }];
        const cajaTattosCollection: ICajaTattos[] = [{ id: 123 }];
        expectedResult = service.addCajaTattosToCollectionIfMissing(cajaTattosCollection, ...cajaTattosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cajaTattos: ICajaTattos = { id: 123 };
        const cajaTattos2: ICajaTattos = { id: 456 };
        expectedResult = service.addCajaTattosToCollectionIfMissing([], cajaTattos, cajaTattos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cajaTattos);
        expect(expectedResult).toContain(cajaTattos2);
      });

      it('should accept null and undefined values', () => {
        const cajaTattos: ICajaTattos = { id: 123 };
        expectedResult = service.addCajaTattosToCollectionIfMissing([], null, cajaTattos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cajaTattos);
      });

      it('should return initial array if no CajaTattos is added', () => {
        const cajaTattosCollection: ICajaTattos[] = [{ id: 123 }];
        expectedResult = service.addCajaTattosToCollectionIfMissing(cajaTattosCollection, undefined, null);
        expect(expectedResult).toEqual(cajaTattosCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
