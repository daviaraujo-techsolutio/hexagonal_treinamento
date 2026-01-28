package com.davijaf.hexagonal.application.core.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void shouldCreateAddressWithDefaultConstructor() {
        Address address = new Address();

        assertNull(address.getStreet());
        assertNull(address.getCity());
        assertNull(address.getState());
    }

    @Test
    void shouldCreateAddressWithAllArgsConstructor() {
        Address address = new Address("Rua Hexagonal", "Uberlândia", "Minas Gerais");

        assertEquals("Rua Hexagonal", address.getStreet());
        assertEquals("Uberlândia", address.getCity());
        assertEquals("Minas Gerais", address.getState());
    }

    @Test
    void shouldSetAndGetStreet() {
        Address address = new Address();
        address.setStreet("Rua das Flores");

        assertEquals("Rua das Flores", address.getStreet());
    }

    @Test
    void shouldSetAndGetCity() {
        Address address = new Address();
        address.setCity("São Paulo");

        assertEquals("São Paulo", address.getCity());
    }

    @Test
    void shouldSetAndGetState() {
        Address address = new Address();
        address.setState("SP");

        assertEquals("SP", address.getState());
    }
}
