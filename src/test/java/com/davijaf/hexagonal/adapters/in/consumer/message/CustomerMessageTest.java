package com.davijaf.hexagonal.adapters.in.consumer.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMessageTest {

    @Test
    void shouldCreateCustomerMessageWithNoArgsConstructor() {
        CustomerMessage message = new CustomerMessage();

        assertNull(message.getId());
        assertNull(message.getName());
        assertNull(message.getZipCode());
        assertNull(message.getCpf());
        assertNull(message.getIsValidCpf());
    }

    @Test
    void shouldCreateCustomerMessageWithAllArgsConstructor() {
        CustomerMessage message = new CustomerMessage("123", "John Doe", "12345-678", "12345678901", true);

        assertEquals("123", message.getId());
        assertEquals("John Doe", message.getName());
        assertEquals("12345-678", message.getZipCode());
        assertEquals("12345678901", message.getCpf());
        assertTrue(message.getIsValidCpf());
    }

    @Test
    void shouldSetAndGetAllFields() {
        CustomerMessage message = new CustomerMessage();
        message.setId("456");
        message.setName("Jane Doe");
        message.setZipCode("98765-432");
        message.setCpf("98765432100");
        message.setIsValidCpf(false);

        assertEquals("456", message.getId());
        assertEquals("Jane Doe", message.getName());
        assertEquals("98765-432", message.getZipCode());
        assertEquals("98765432100", message.getCpf());
        assertFalse(message.getIsValidCpf());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        CustomerMessage message1 = new CustomerMessage("123", "John", "12345", "cpf123", true);
        CustomerMessage message2 = new CustomerMessage("123", "John", "12345", "cpf123", true);

        assertEquals(message1, message2);
        assertEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    void shouldTestNotEquals() {
        CustomerMessage message1 = new CustomerMessage("123", "John", "12345", "cpf123", true);
        CustomerMessage message2 = new CustomerMessage("456", "Jane", "67890", "cpf456", false);

        assertNotEquals(message1, message2);
    }

    @Test
    void shouldTestToString() {
        CustomerMessage message = new CustomerMessage("123", "John", "12345", "cpf123", true);

        String toString = message.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("123"));
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("12345"));
    }
}
