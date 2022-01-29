package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.VentasDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ventas} and its DTO {@link VentasDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VentasMapper extends EntityMapper<VentasDTO, Ventas> {}
