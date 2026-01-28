package com.davijaf.hexagonal.application.core.usecase;

import com.davijaf.hexagonal.application.core.domain.Address;
import com.davijaf.hexagonal.application.core.domain.Customer;
import com.davijaf.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.davijaf.hexagonal.application.ports.out.FindAddressByZipCodeOutputPort;
import com.davijaf.hexagonal.application.ports.out.UpdateCustomerOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerUseCaseTest {

    @Mock
    private FindCustomerByIdInputPort findCustomerByIdInputPort;

    @Mock
    private FindAddressByZipCodeOutputPort findAddressByZipCodeOutputPort;

    @Mock
    private UpdateCustomerOutputPort updateCustomerOutputPort;

    private UpdateCustomerUseCase updateCustomerUseCase;

    @BeforeEach
    void setUp() {
        updateCustomerUseCase = new UpdateCustomerUseCase(
                findCustomerByIdInputPort,
                findAddressByZipCodeOutputPort,
                updateCustomerOutputPort
        );
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        // Arrange
        String customerId = "123";
        String zipCode = "38400001";
        Address existingAddress = new Address("Rua Antiga", "Cidade Antiga", "Estado Antigo");
        Address newAddress = new Address("Rua das Flores", "São Paulo", "São Paulo");

        Customer existingCustomer = new Customer(customerId, "John", existingAddress, "12345678900", false);
        Customer customerToUpdate = new Customer();
        customerToUpdate.setId(customerId);
        customerToUpdate.setName("John Updated");
        customerToUpdate.setCpf("12345678900");

        when(findCustomerByIdInputPort.find(customerId)).thenReturn(existingCustomer);
        when(findAddressByZipCodeOutputPort.find(zipCode)).thenReturn(newAddress);

        // Act
        updateCustomerUseCase.update(customerToUpdate, zipCode);

        // Assert
        assertEquals(newAddress, customerToUpdate.getAddress());
        verify(findCustomerByIdInputPort).find(customerId);
        verify(findAddressByZipCodeOutputPort).find(zipCode);
        verify(updateCustomerOutputPort).update(customerToUpdate);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        // Arrange
        String customerId = "999";
        String zipCode = "38400000";
        Customer customerToUpdate = new Customer();
        customerToUpdate.setId(customerId);

        when(findCustomerByIdInputPort.find(customerId))
                .thenThrow(new RuntimeException("Customer not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            updateCustomerUseCase.update(customerToUpdate, zipCode);
        });

        verify(findCustomerByIdInputPort).find(customerId);
        verify(findAddressByZipCodeOutputPort, never()).find(anyString());
        verify(updateCustomerOutputPort, never()).update(any());
    }

    @Test
    void shouldSetNewAddressBeforeUpdate() {
        // Arrange
        String customerId = "456";
        String zipCode = "38400000";
        Address newAddress = new Address("Rua Hexagonal", "Uberlândia", "Minas Gerais");

        Customer existingCustomer = new Customer(customerId, "Jane", null, "98765432100", true);
        Customer customerToUpdate = new Customer();
        customerToUpdate.setId(customerId);
        customerToUpdate.setName("Jane");
        customerToUpdate.setCpf("98765432100");

        when(findCustomerByIdInputPort.find(customerId)).thenReturn(existingCustomer);
        when(findAddressByZipCodeOutputPort.find(zipCode)).thenReturn(newAddress);

        // Act
        updateCustomerUseCase.update(customerToUpdate, zipCode);

        // Assert
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(updateCustomerOutputPort).update(customerCaptor.capture());

        Customer capturedCustomer = customerCaptor.getValue();
        assertNotNull(capturedCustomer.getAddress());
        assertEquals("Rua Hexagonal", capturedCustomer.getAddress().getStreet());
        assertEquals("Uberlândia", capturedCustomer.getAddress().getCity());
        assertEquals("Minas Gerais", capturedCustomer.getAddress().getState());
    }

    @Test
    void shouldCallMethodsInCorrectOrder() {
        // Arrange
        String customerId = "123";
        String zipCode = "38400000";
        Address address = new Address("Street", "City", "State");
        Customer existingCustomer = new Customer(customerId, "John", null, "12345678900", false);
        Customer customerToUpdate = new Customer();
        customerToUpdate.setId(customerId);

        when(findCustomerByIdInputPort.find(customerId)).thenReturn(existingCustomer);
        when(findAddressByZipCodeOutputPort.find(zipCode)).thenReturn(address);

        // Act
        updateCustomerUseCase.update(customerToUpdate, zipCode);

        // Assert
        var inOrder = inOrder(findCustomerByIdInputPort, findAddressByZipCodeOutputPort, updateCustomerOutputPort);
        inOrder.verify(findCustomerByIdInputPort).find(customerId);
        inOrder.verify(findAddressByZipCodeOutputPort).find(zipCode);
        inOrder.verify(updateCustomerOutputPort).update(customerToUpdate);
    }
}
