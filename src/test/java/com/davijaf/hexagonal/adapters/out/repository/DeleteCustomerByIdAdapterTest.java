package com.davijaf.hexagonal.adapters.out.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteCustomerByIdAdapterTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private DeleteCustomerByIdAdapter deleteCustomerByIdAdapter;

    private String customerId;

    @BeforeEach
    void setUp() {
        customerId = "123";
    }

    @Test
    void shouldDeleteCustomerById() {
        deleteCustomerByIdAdapter.delete(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    void shouldDeleteCustomerWithDifferentId() {
        String anotherId = "456";

        deleteCustomerByIdAdapter.delete(anotherId);

        verify(customerRepository, times(1)).deleteById(anotherId);
    }
}
