package com.davijaf.hexagonal.adapters.out.repository;

import com.davijaf.hexagonal.adapters.out.repository.entity.AddressEntity;
import com.davijaf.hexagonal.adapters.out.repository.entity.CustomerEntity;
import com.davijaf.hexagonal.adapters.out.repository.mapper.CustomerEntityMapper;
import com.davijaf.hexagonal.application.core.domain.Address;
import com.davijaf.hexagonal.application.core.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerAdapterTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerEntityMapper customerEntityMapper;

    @InjectMocks
    private UpdateCustomerAdapter updateCustomerAdapter;

    private Customer customer;
    private CustomerEntity customerEntity;

    @BeforeEach
    void setUp() {
        Address address = new Address();
        address.setStreet("Main St");
        address.setCity("NYC");
        address.setState("NY");

        customer = new Customer();
        customer.setId("123");
        customer.setName("John Doe");
        customer.setCpf("12345678901");
        customer.setIsValidCpf(true);
        customer.setAddress(address);

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setStreet("Main St");
        addressEntity.setCity("NYC");
        addressEntity.setState("NY");

        customerEntity = new CustomerEntity();
        customerEntity.setId("123");
        customerEntity.setName("John Doe");
        customerEntity.setCpf("12345678901");
        customerEntity.setIsValidCpf(true);
        customerEntity.setAddress(addressEntity);
    }

    @Test
    void shouldUpdateCustomer() {
        when(customerEntityMapper.toCustomerEntity(any(Customer.class))).thenReturn(customerEntity);

        updateCustomerAdapter.update(customer);

        verify(customerEntityMapper, times(1)).toCustomerEntity(customer);
        verify(customerRepository, times(1)).save(customerEntity);
    }

    @Test
    void shouldUpdateCustomerWithDifferentData() {
        Customer anotherCustomer = new Customer();
        anotherCustomer.setId("456");
        anotherCustomer.setName("Jane Doe");

        CustomerEntity anotherEntity = new CustomerEntity();
        anotherEntity.setId("456");
        anotherEntity.setName("Jane Doe");

        when(customerEntityMapper.toCustomerEntity(anotherCustomer)).thenReturn(anotherEntity);

        updateCustomerAdapter.update(anotherCustomer);

        verify(customerEntityMapper, times(1)).toCustomerEntity(anotherCustomer);
        verify(customerRepository, times(1)).save(anotherEntity);
    }
}
