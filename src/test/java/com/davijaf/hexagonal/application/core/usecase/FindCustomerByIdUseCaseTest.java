package com.davijaf.hexagonal.application.core.usecase;

import com.davijaf.hexagonal.application.core.domain.Address;
import com.davijaf.hexagonal.application.core.domain.Customer;
import com.davijaf.hexagonal.application.core.exceptions.ObjectNotFoundException;
import com.davijaf.hexagonal.application.ports.out.FindCustomerByIdOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindCustomerByIdUseCaseTest {

    @Mock
    private FindCustomerByIdOutputPort findCustomerByIdOutputPort;

    private FindCustomerByIdUseCase findCustomerByIdUseCase;

    @BeforeEach
    void setUp() {
        findCustomerByIdUseCase = new FindCustomerByIdUseCase(findCustomerByIdOutputPort);
    }

    @Test
    void shouldFindCustomerByIdSuccessfully() {
        // Arrange
        String customerId = "123";
        Address address = new Address("Rua Hexagonal", "Uberlândia", "Minas Gerais");
        Customer expectedCustomer = new Customer(customerId, "John", address, "12345678900", false);

        when(findCustomerByIdOutputPort.find(customerId)).thenReturn(Optional.of(expectedCustomer));

        // Act
        Customer result = findCustomerByIdUseCase.find(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals("John", result.getName());
        assertEquals("12345678900", result.getCpf());
        verify(findCustomerByIdOutputPort).find(customerId);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        // Arrange
        String customerId = "999";

        when(findCustomerByIdOutputPort.find(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            findCustomerByIdUseCase.find(customerId);
        });

        assertEquals("Object with id 999 not found", exception.getMessage());
        verify(findCustomerByIdOutputPort).find(customerId);
    }

    @Test
    void shouldReturnCustomerWithAllFields() {
        // Arrange
        String customerId = "456";
        Address address = new Address("Rua das Flores", "São Paulo", "São Paulo");
        Customer expectedCustomer = new Customer(customerId, "Jane", address, "98765432100", true);

        when(findCustomerByIdOutputPort.find(customerId)).thenReturn(Optional.of(expectedCustomer));

        // Act
        Customer result = findCustomerByIdUseCase.find(customerId);

        // Assert
        assertEquals("456", result.getId());
        assertEquals("Jane", result.getName());
        assertEquals("Rua das Flores", result.getAddress().getStreet());
        assertEquals("São Paulo", result.getAddress().getCity());
        assertEquals("São Paulo", result.getAddress().getState());
        assertEquals("98765432100", result.getCpf());
        assertTrue(result.getIsValidCpf());
    }
}
