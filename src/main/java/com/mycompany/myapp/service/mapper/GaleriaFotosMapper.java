package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.GaleriaFotosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GaleriaFotos} and its DTO {@link GaleriaFotosDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GaleriaFotosMapper extends EntityMapper<GaleriaFotosDTO, GaleriaFotos> {}
