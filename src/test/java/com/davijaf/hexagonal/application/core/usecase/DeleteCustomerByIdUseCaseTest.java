package com.davijaf.hexagonal.application.core.usecase;

import com.davijaf.hexagonal.application.core.domain.Customer;
import com.davijaf.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.davijaf.hexagonal.application.ports.out.DeleteCustomerByIdOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCustomerByIdUseCaseTest {

    @Mock
    private FindCustomerByIdInputPort findCustomerByIdInputPort;

    @Mock
    private DeleteCustomerByIdOutputPort deleteCustomerByIdOutputPort;

    private DeleteCustomerByIdUseCase deleteCustomerByIdUseCase;

    @BeforeEach
    void setUp() {
        deleteCustomerByIdUseCase = new DeleteCustomerByIdUseCase(
                findCustomerByIdInputPort,
                deleteCustomerByIdOutputPort
        );
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
        // Arrange
        String customerId = "123";
        Customer existingCustomer = new Customer(customerId, "John", null, "12345678900", false);

        when(findCustomerByIdInputPort.find(customerId)).thenReturn(existingCustomer);

        // Act
        deleteCustomerByIdUseCase.delete(customerId);

        // Assert
        verify(findCustomerByIdInputPort).find(customerId);
        verify(deleteCustomerByIdOutputPort).delete(customerId);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        // Arrange
        String customerId = "999";

        when(findCustomerByIdInputPort.find(customerId))
                .thenThrow(new RuntimeException("Customer not found"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deleteCustomerByIdUseCase.delete(customerId);
        });

        assertEquals("Customer not found", exception.getMessage());
        verify(findCustomerByIdInputPort).find(customerId);
        verify(deleteCustomerByIdOutputPort, never()).delete(anyString());
    }

    @Test
    void shouldVerifyCustomerExistsBeforeDelete() {
        // Arrange
        String customerId = "456";
        Customer existingCustomer = new Customer(customerId, "Jane", null, "98765432100", true);

        when(findCustomerByIdInputPort.find(customerId)).thenReturn(existingCustomer);

        // Act
        deleteCustomerByIdUseCase.delete(customerId);

        // Assert
        var inOrder = inOrder(findCustomerByIdInputPort, deleteCustomerByIdOutputPort);
        inOrder.verify(findCustomerByIdInputPort).find(customerId);
        inOrder.verify(deleteCustomerByIdOutputPort).delete(customerId);
    }

    @Test
    void shouldNotDeleteWhenFindThrowsException() {
        // Arrange
        String customerId = "789";

        when(findCustomerByIdInputPort.find(customerId))
                .thenThrow(new RuntimeException("Customer not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            deleteCustomerByIdUseCase.delete(customerId);
        });

        verify(deleteCustomerByIdOutputPort, never()).delete(customerId);
    }
}
