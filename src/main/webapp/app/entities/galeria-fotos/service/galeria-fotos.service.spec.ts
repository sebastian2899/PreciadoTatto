import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGaleriaFotos, GaleriaFotos } from '../galeria-fotos.model';

import { GaleriaFotosService } from './galeria-fotos.service';

describe('GaleriaFotos Service', () => {
  let service: GaleriaFotosService;
  let httpMock: HttpTestingController;
  let elemDefault: IGaleriaFotos;
  let expectedResult: IGaleriaFotos | IGaleriaFotos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GaleriaFotosService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombreDisenio: 'AAAAAAA',
      disenioContentType: 'image/png',
      disenio: 'AAAAAAA',
      descripcion: 'AAAAAAA',
      precioDisenio: 0,
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

    it('should create a GaleriaFotos', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new GaleriaFotos()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GaleriaFotos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombreDisenio: 'BBBBBB',
          disenio: 'BBBBBB',
          descripcion: 'BBBBBB',
          precioDisenio: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GaleriaFotos', () => {
      const patchObject = Object.assign(
        {
          precioDisenio: 1,
        },
        new GaleriaFotos()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GaleriaFotos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombreDisenio: 'BBBBBB',
          disenio: 'BBBBBB',
          descripcion: 'BBBBBB',
          precioDisenio: 1,
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

    it('should delete a GaleriaFotos', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGaleriaFotosToCollectionIfMissing', () => {
      it('should add a GaleriaFotos to an empty array', () => {
        const galeriaFotos: IGaleriaFotos = { id: 123 };
        expectedResult = service.addGaleriaFotosToCollectionIfMissing([], galeriaFotos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(galeriaFotos);
      });

      it('should not add a GaleriaFotos to an array that contains it', () => {
        const galeriaFotos: IGaleriaFotos = { id: 123 };
        const galeriaFotosCollection: IGaleriaFotos[] = [
          {
            ...galeriaFotos,
          },
          { id: 456 },
        ];
        expectedResult = service.addGaleriaFotosToCollectionIfMissing(galeriaFotosCollection, galeriaFotos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GaleriaFotos to an array that doesn't contain it", () => {
        const galeriaFotos: IGaleriaFotos = { id: 123 };
        const galeriaFotosCollection: IGaleriaFotos[] = [{ id: 456 }];
        expectedResult = service.addGaleriaFotosToCollectionIfMissing(galeriaFotosCollection, galeriaFotos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(galeriaFotos);
      });

      it('should add only unique GaleriaFotos to an array', () => {
        const galeriaFotosArray: IGaleriaFotos[] = [{ id: 123 }, { id: 456 }, { id: 46591 }];
        const galeriaFotosCollection: IGaleriaFotos[] = [{ id: 123 }];
        expectedResult = service.addGaleriaFotosToCollectionIfMissing(galeriaFotosCollection, ...galeriaFotosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const galeriaFotos: IGaleriaFotos = { id: 123 };
        const galeriaFotos2: IGaleriaFotos = { id: 456 };
        expectedResult = service.addGaleriaFotosToCollectionIfMissing([], galeriaFotos, galeriaFotos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(galeriaFotos);
        expect(expectedResult).toContain(galeriaFotos2);
      });

      it('should accept null and undefined values', () => {
        const galeriaFotos: IGaleriaFotos = { id: 123 };
        expectedResult = service.addGaleriaFotosToCollectionIfMissing([], null, galeriaFotos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(galeriaFotos);
      });

      it('should return initial array if no GaleriaFotos is added', () => {
        const galeriaFotosCollection: IGaleriaFotos[] = [{ id: 123 }];
        expectedResult = service.addGaleriaFotosToCollectionIfMissing(galeriaFotosCollection, undefined, null);
        expect(expectedResult).toEqual(galeriaFotosCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
