package com.davijaf.hexagonal.adapters.out.repository.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerEntityTest {

    @Test
    void shouldCreateCustomerEntityWithAllFields() {
        CustomerEntity entity = new CustomerEntity();
        entity.setId("123");
        entity.setName("John Doe");
        entity.setCpf("12345678901");
        entity.setIsValidCpf(true);

        AddressEntity address = new AddressEntity();
        address.setStreet("Main St");
        address.setCity("New York");
        address.setState("NY");
        entity.setAddress(address);

        assertEquals("123", entity.getId());
        assertEquals("John Doe", entity.getName());
        assertEquals("12345678901", entity.getCpf());
        assertTrue(entity.getIsValidCpf());
        assertNotNull(entity.getAddress());
        assertEquals("Main St", entity.getAddress().getStreet());
    }

    @Test
    void shouldHandleNullValues() {
        CustomerEntity entity = new CustomerEntity();

        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getCpf());
        assertNull(entity.getIsValidCpf());
        assertNull(entity.getAddress());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        CustomerEntity entity1 = new CustomerEntity();
        entity1.setId("123");
        entity1.setName("John");

        CustomerEntity entity2 = new CustomerEntity();
        entity2.setId("123");
        entity2.setName("John");

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void shouldTestToString() {
        CustomerEntity entity = new CustomerEntity();
        entity.setId("123");
        entity.setName("John");

        String toString = entity.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("123"));
        assertTrue(toString.contains("John"));
    }
}
