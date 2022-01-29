package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CajaIngresosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CajaIngresos} and its DTO {@link CajaIngresosDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CajaIngresosMapper extends EntityMapper<CajaIngresosDTO, CajaIngresos> {}
