package com.davijaf.hexagonal.adapters.out.repository.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressEntityTest {

    @Test
    void shouldCreateAddressEntityWithAllFields() {
        AddressEntity entity = new AddressEntity();
        entity.setStreet("Main Street");
        entity.setCity("New York");
        entity.setState("NY");

        assertEquals("Main Street", entity.getStreet());
        assertEquals("New York", entity.getCity());
        assertEquals("NY", entity.getState());
    }

    @Test
    void shouldHandleNullValues() {
        AddressEntity entity = new AddressEntity();

        assertNull(entity.getStreet());
        assertNull(entity.getCity());
        assertNull(entity.getState());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        AddressEntity entity1 = new AddressEntity();
        entity1.setStreet("Main St");
        entity1.setCity("NYC");
        entity1.setState("NY");

        AddressEntity entity2 = new AddressEntity();
        entity2.setStreet("Main St");
        entity2.setCity("NYC");
        entity2.setState("NY");

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void shouldTestNotEquals() {
        AddressEntity entity1 = new AddressEntity();
        entity1.setStreet("Main St");

        AddressEntity entity2 = new AddressEntity();
        entity2.setStreet("Other St");

        assertNotEquals(entity1, entity2);
    }

    @Test
    void shouldTestToString() {
        AddressEntity entity = new AddressEntity();
        entity.setStreet("Main St");
        entity.setCity("NYC");
        entity.setState("NY");

        String toString = entity.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Main St"));
        assertTrue(toString.contains("NYC"));
        assertTrue(toString.contains("NY"));
    }
}
