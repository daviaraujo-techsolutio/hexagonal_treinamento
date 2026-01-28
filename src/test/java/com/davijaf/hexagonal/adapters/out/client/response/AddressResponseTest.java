package com.davijaf.hexagonal.adapters.out.client.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressResponseTest {

    @Test
    void shouldCreateAddressResponseWithAllFields() {
        AddressResponse response = new AddressResponse();
        response.setStreet("Avenida Paulista");
        response.setCity("Sao Paulo");
        response.setState("SP");

        assertEquals("Avenida Paulista", response.getStreet());
        assertEquals("Sao Paulo", response.getCity());
        assertEquals("SP", response.getState());
    }

    @Test
    void shouldHandleNullValues() {
        AddressResponse response = new AddressResponse();

        assertNull(response.getStreet());
        assertNull(response.getCity());
        assertNull(response.getState());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        AddressResponse response1 = new AddressResponse();
        response1.setStreet("Rua A");
        response1.setCity("City");
        response1.setState("ST");

        AddressResponse response2 = new AddressResponse();
        response2.setStreet("Rua A");
        response2.setCity("City");
        response2.setState("ST");

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void shouldTestNotEquals() {
        AddressResponse response1 = new AddressResponse();
        response1.setStreet("Rua A");

        AddressResponse response2 = new AddressResponse();
        response2.setStreet("Rua B");

        assertNotEquals(response1, response2);
    }

    @Test
    void shouldTestToString() {
        AddressResponse response = new AddressResponse();
        response.setStreet("Rua Test");
        response.setCity("Test City");
        response.setState("TC");

        String toString = response.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Rua Test"));
        assertTrue(toString.contains("Test City"));
        assertTrue(toString.contains("TC"));
    }
}
