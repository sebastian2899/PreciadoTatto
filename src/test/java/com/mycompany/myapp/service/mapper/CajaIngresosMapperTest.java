package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CajaIngresosMapperTest {

    private CajaIngresosMapper cajaIngresosMapper;

    @BeforeEach
    public void setUp() {
        cajaIngresosMapper = new CajaIngresosMapperImpl();
    }
}
