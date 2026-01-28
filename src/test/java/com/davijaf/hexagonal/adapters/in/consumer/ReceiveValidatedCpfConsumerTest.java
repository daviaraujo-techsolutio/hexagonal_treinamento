package com.davijaf.hexagonal.adapters.in.consumer;

import com.davijaf.hexagonal.adapters.in.consumer.mapper.CustomerMessageMapper;
import com.davijaf.hexagonal.adapters.in.consumer.message.CustomerMessage;
import com.davijaf.hexagonal.application.core.domain.Customer;
import com.davijaf.hexagonal.application.ports.in.UpdateCustomerInputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiveValidatedCpfConsumerTest {

    @Mock
    private UpdateCustomerInputPort updateCustomerInputPort;

    @Mock
    private CustomerMessageMapper customerMessageMapper;

    @InjectMocks
    private ReceiveValidatedCpfConsumer receiveValidatedCpfConsumer;

    private CustomerMessage customerMessage;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customerMessage = new CustomerMessage();
        customerMessage.setId("123");
        customerMessage.setName("John Doe");
        customerMessage.setZipCode("01310-100");
        customerMessage.setCpf("12345678901");
        customerMessage.setIsValidCpf(true);

        customer = new Customer();
        customer.setId("123");
        customer.setName("John Doe");
        customer.setCpf("12345678901");
        customer.setIsValidCpf(true);
    }

    @Test
    void shouldReceiveAndProcessValidatedCpf() {
        when(customerMessageMapper.toCustomer(any(CustomerMessage.class))).thenReturn(customer);

        receiveValidatedCpfConsumer.receive(customerMessage);

        verify(customerMessageMapper, times(1)).toCustomer(customerMessage);
        verify(updateCustomerInputPort, times(1)).update(customer, "01310-100");
    }

    @Test
    void shouldReceiveMessageWithInvalidCpf() {
        CustomerMessage invalidMessage = new CustomerMessage();
        invalidMessage.setId("456");
        invalidMessage.setName("Jane Doe");
        invalidMessage.setZipCode("22041-080");
        invalidMessage.setCpf("98765432100");
        invalidMessage.setIsValidCpf(false);

        Customer invalidCustomer = new Customer();
        invalidCustomer.setId("456");
        invalidCustomer.setName("Jane Doe");
        invalidCustomer.setCpf("98765432100");
        invalidCustomer.setIsValidCpf(false);

        when(customerMessageMapper.toCustomer(invalidMessage)).thenReturn(invalidCustomer);

        receiveValidatedCpfConsumer.receive(invalidMessage);

        verify(customerMessageMapper, times(1)).toCustomer(invalidMessage);
        verify(updateCustomerInputPort, times(1)).update(invalidCustomer, "22041-080");
    }

    @Test
    void shouldProcessMessageWithCorrectZipCode() {
        String expectedZipCode = "01310-100";

        when(customerMessageMapper.toCustomer(customerMessage)).thenReturn(customer);

        receiveValidatedCpfConsumer.receive(customerMessage);

        verify(updateCustomerInputPort).update(eq(customer), eq(expectedZipCode));
    }
}
