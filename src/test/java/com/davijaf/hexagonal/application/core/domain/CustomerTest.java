package com.davijaf.hexagonal.application.core.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void shouldCreateCustomerWithDefaultConstructor() {
        Customer customer = new Customer();

        assertNull(customer.getId());
        assertNull(customer.getName());
        assertNull(customer.getAddress());
        assertNull(customer.getCpf());
        assertFalse(customer.getIsValidCpf());
    }

    @Test
    void shouldCreateCustomerWithAllArgsConstructor() {
        Address address = new Address("Rua Test", "City", "State");
        Customer customer = new Customer("123", "John", address, "12345678900", true);

        assertEquals("123", customer.getId());
        assertEquals("John", customer.getName());
        assertEquals(address, customer.getAddress());
        assertEquals("12345678900", customer.getCpf());
        assertTrue(customer.getIsValidCpf());
    }

    @Test
    void shouldSetAndGetId() {
        Customer customer = new Customer();
        customer.setId("456");

        assertEquals("456", customer.getId());
    }

    @Test
    void shouldSetAndGetName() {
        Customer customer = new Customer();
        customer.setName("Jane");

        assertEquals("Jane", customer.getName());
    }

    @Test
    void shouldSetAndGetAddress() {
        Customer customer = new Customer();
        Address address = new Address("Street", "City", "State");
        customer.setAddress(address);

        assertEquals(address, customer.getAddress());
    }

    @Test
    void shouldSetAndGetCpf() {
        Customer customer = new Customer();
        customer.setCpf("98765432100");

        assertEquals("98765432100", customer.getCpf());
    }

    @Test
    void shouldSetAndGetIsValidCpf() {
        Customer customer = new Customer();
        customer.setIsValidCpf(true);

        assertTrue(customer.getIsValidCpf());
    }
}
