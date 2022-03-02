package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class ClienteMapperTest {

    private ClienteMapper clienteMapper;

    @BeforeEach
    public void setUp() {
        clienteMapper = new ClienteMapperImpl();
    }
}
