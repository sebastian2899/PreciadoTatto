package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GaleriaFotosMapperTest {

    private GaleriaFotosMapper galeriaFotosMapper;

    @BeforeEach
    public void setUp() {
        galeriaFotosMapper = new GaleriaFotosMapperImpl();
    }
}
