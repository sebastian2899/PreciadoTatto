import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVentas, Ventas } from '../ventas.model';

import { VentasService } from './ventas.service';

describe('Ventas Service', () => {
  let service: VentasService;
  let httpMock: HttpTestingController;
  let elemDefault: IVentas;
  let expectedResult: IVentas | IVentas[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VentasService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      fechaCreacion: 'AAAAAAA',
      idCliente: 0,
      valorVenta: 0,
      valorPagado: 0,
      valorDeuda: 0,
      estado: 'AAAAAAA',
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

    it('should create a Ventas', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Ventas()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ventas', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: 'BBBBBB',
          idCliente: 1,
          valorVenta: 1,
          valorPagado: 1,
          valorDeuda: 1,
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ventas', () => {
      const patchObject = Object.assign(
        {
          idCliente: 1,
          valorVenta: 1,
          valorPagado: 1,
        },
        new Ventas()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ventas', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: 'BBBBBB',
          idCliente: 1,
          valorVenta: 1,
          valorPagado: 1,
          valorDeuda: 1,
          estado: 'BBBBBB',
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

    it('should delete a Ventas', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVentasToCollectionIfMissing', () => {
      it('should add a Ventas to an empty array', () => {
        const ventas: IVentas = { id: 123 };
        expectedResult = service.addVentasToCollectionIfMissing([], ventas);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ventas);
      });

      it('should not add a Ventas to an array that contains it', () => {
        const ventas: IVentas = { id: 123 };
        const ventasCollection: IVentas[] = [
          {
            ...ventas,
          },
          { id: 456 },
        ];
        expectedResult = service.addVentasToCollectionIfMissing(ventasCollection, ventas);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ventas to an array that doesn't contain it", () => {
        const ventas: IVentas = { id: 123 };
        const ventasCollection: IVentas[] = [{ id: 456 }];
        expectedResult = service.addVentasToCollectionIfMissing(ventasCollection, ventas);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ventas);
      });

      it('should add only unique Ventas to an array', () => {
        const ventasArray: IVentas[] = [{ id: 123 }, { id: 456 }, { id: 25966 }];
        const ventasCollection: IVentas[] = [{ id: 123 }];
        expectedResult = service.addVentasToCollectionIfMissing(ventasCollection, ...ventasArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ventas: IVentas = { id: 123 };
        const ventas2: IVentas = { id: 456 };
        expectedResult = service.addVentasToCollectionIfMissing([], ventas, ventas2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ventas);
        expect(expectedResult).toContain(ventas2);
      });

      it('should accept null and undefined values', () => {
        const ventas: IVentas = { id: 123 };
        expectedResult = service.addVentasToCollectionIfMissing([], null, ventas, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ventas);
      });

      it('should return initial array if no Ventas is added', () => {
        const ventasCollection: IVentas[] = [{ id: 123 }];
        expectedResult = service.addVentasToCollectionIfMissing(ventasCollection, undefined, null);
        expect(expectedResult).toEqual(ventasCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
