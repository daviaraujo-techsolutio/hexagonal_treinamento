package com.davijaf.hexagonal.application.core.usecase;

import com.davijaf.hexagonal.application.core.domain.Address;
import com.davijaf.hexagonal.application.core.domain.Customer;
import com.davijaf.hexagonal.application.ports.out.FindAddressByZipCodeOutputPort;
import com.davijaf.hexagonal.application.ports.out.InsertCustomerOutputPort;
import com.davijaf.hexagonal.application.ports.out.SendCpfForValidationOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsertCustomerUseCaseTest {

    @Mock
    private FindAddressByZipCodeOutputPort findAddressByZipCodeOutputPort;

    @Mock
    private InsertCustomerOutputPort insertCustomerOutputPort;

    @Mock
    private SendCpfForValidationOutputPort sendCpfForValidationOutputPort;

    private InsertCustomerUseCase insertCustomerUseCase;

    @BeforeEach
    void setUp() {
        insertCustomerUseCase = new InsertCustomerUseCase(
                findAddressByZipCodeOutputPort,
                insertCustomerOutputPort,
                sendCpfForValidationOutputPort
        );
    }

    @Test
    void shouldInsertCustomerSuccessfully() {
        // Arrange
        String zipCode = "38400000";
        String cpf = "12345678900";
        Address address = new Address("Rua Hexagonal", "Uberlândia", "Minas Gerais");
        Customer customer = new Customer();
        customer.setName("John");
        customer.setCpf(cpf);

        when(findAddressByZipCodeOutputPort.find(zipCode)).thenReturn(address);

        // Act
        insertCustomerUseCase.insert(customer, zipCode);

        // Assert
        assertEquals(address, customer.getAddress());
        verify(findAddressByZipCodeOutputPort).find(zipCode);
        verify(insertCustomerOutputPort).insert(customer);
        verify(sendCpfForValidationOutputPort).send(cpf);
    }

    @Test
    void shouldSetAddressOnCustomerBeforeInsert() {
        // Arrange
        String zipCode = "38400001";
        Address address = new Address("Rua das Flores", "São Paulo", "São Paulo");
        Customer customer = new Customer();
        customer.setName("Jane");
        customer.setCpf("98765432100");

        when(findAddressByZipCodeOutputPort.find(zipCode)).thenReturn(address);

        // Act
        insertCustomerUseCase.insert(customer, zipCode);

        // Assert
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(insertCustomerOutputPort).insert(customerCaptor.capture());

        Customer capturedCustomer = customerCaptor.getValue();
        assertNotNull(capturedCustomer.getAddress());
        assertEquals("Rua das Flores", capturedCustomer.getAddress().getStreet());
        assertEquals("São Paulo", capturedCustomer.getAddress().getCity());
    }

    @Test
    void shouldSendCpfForValidationAfterInsert() {
        // Arrange
        String zipCode = "38400000";
        String cpf = "11122233344";
        Address address = new Address("Street", "City", "State");
        Customer customer = new Customer();
        customer.setCpf(cpf);

        when(findAddressByZipCodeOutputPort.find(zipCode)).thenReturn(address);

        // Act
        insertCustomerUseCase.insert(customer, zipCode);

        // Assert
        verify(sendCpfForValidationOutputPort).send(cpf);
    }

    @Test
    void shouldCallOutputPortsInCorrectOrder() {
        // Arrange
        String zipCode = "38400000";
        Address address = new Address("Street", "City", "State");
        Customer customer = new Customer();
        customer.setCpf("12345678900");

        when(findAddressByZipCodeOutputPort.find(zipCode)).thenReturn(address);

        // Act
        insertCustomerUseCase.insert(customer, zipCode);

        // Assert
        var inOrder = inOrder(findAddressByZipCodeOutputPort, insertCustomerOutputPort, sendCpfForValidationOutputPort);
        inOrder.verify(findAddressByZipCodeOutputPort).find(zipCode);
        inOrder.verify(insertCustomerOutputPort).insert(customer);
        inOrder.verify(sendCpfForValidationOutputPort).send(customer.getCpf());
    }
}
