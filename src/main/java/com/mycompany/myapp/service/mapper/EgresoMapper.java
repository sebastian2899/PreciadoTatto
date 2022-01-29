package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.EgresoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Egreso} and its DTO {@link EgresoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EgresoMapper extends EntityMapper<EgresoDTO, Egreso> {}
