package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ComprasDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Compras} and its DTO {@link ComprasDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ComprasMapper extends EntityMapper<ComprasDTO, Compras> {}
