package com.davijaf.hexagonal.adapters.out.repository;

import com.davijaf.hexagonal.adapters.out.repository.entity.AddressEntity;
import com.davijaf.hexagonal.adapters.out.repository.entity.CustomerEntity;
import com.davijaf.hexagonal.adapters.out.repository.mapper.CustomerEntityMapper;
import com.davijaf.hexagonal.application.core.domain.Address;
import com.davijaf.hexagonal.application.core.domain.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindCustomerByIdAdapterTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerEntityMapper customerEntityMapper;

    @InjectMocks
    private FindCustomerByIdAdapter findCustomerByIdAdapter;

    @Test
    void shouldFindCustomerByIdSuccessfully() {
        // Arrange
        String customerId = "123";

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setStreet("Rua Hexagonal");
        addressEntity.setCity("Uberlândia");
        addressEntity.setState("Minas Gerais");

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerId);
        customerEntity.setName("John");
        customerEntity.setAddress(addressEntity);
        customerEntity.setCpf("12345678900");
        customerEntity.setIsValidCpf(false);

        Address address = new Address("Rua Hexagonal", "Uberlândia", "Minas Gerais");
        Customer expectedCustomer = new Customer(customerId, "John", address, "12345678900", false);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));
        when(customerEntityMapper.toCustomer(customerEntity)).thenReturn(expectedCustomer);

        // Act
        Optional<Customer> result = findCustomerByIdAdapter.find(customerId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(customerId, result.get().getId());
        assertEquals("John", result.get().getName());
        assertEquals("12345678900", result.get().getCpf());
        verify(customerRepository).findById(customerId);
        verify(customerEntityMapper).toCustomer(customerEntity);
    }

    @Test
    void shouldReturnEmptyOptionalWhenCustomerNotFound() {
        // Arrange
        String customerId = "999";

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act
        Optional<Customer> result = findCustomerByIdAdapter.find(customerId);

        // Assert
        assertFalse(result.isPresent());
        verify(customerRepository).findById(customerId);
        verify(customerEntityMapper, never()).toCustomer(any());
    }

    @Test
    void shouldMapEntityToCustomerWhenFound() {
        // Arrange
        String customerId = "456";

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerId);
        customerEntity.setName("Jane");
        customerEntity.setCpf("98765432100");
        customerEntity.setIsValidCpf(true);

        Customer expectedCustomer = new Customer(customerId, "Jane", null, "98765432100", true);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));
        when(customerEntityMapper.toCustomer(customerEntity)).thenReturn(expectedCustomer);

        // Act
        Optional<Customer> result = findCustomerByIdAdapter.find(customerId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Jane", result.get().getName());
        assertTrue(result.get().getIsValidCpf());
    }
}
