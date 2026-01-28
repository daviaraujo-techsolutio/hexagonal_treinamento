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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsertCustomerAdapterTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerEntityMapper customerEntityMapper;

    @InjectMocks
    private InsertCustomerAdapter insertCustomerAdapter;

    @Test
    void shouldInsertCustomerSuccessfully() {
        // Arrange
        Address address = new Address("Rua Hexagonal", "Uberlândia", "Minas Gerais");
        Customer customer = new Customer("123", "John", address, "12345678900", false);

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setStreet("Rua Hexagonal");
        addressEntity.setCity("Uberlândia");
        addressEntity.setState("Minas Gerais");

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId("123");
        customerEntity.setName("John");
        customerEntity.setAddress(addressEntity);
        customerEntity.setCpf("12345678900");
        customerEntity.setIsValidCpf(false);

        when(customerEntityMapper.toCustomerEntity(customer)).thenReturn(customerEntity);

        // Act
        insertCustomerAdapter.insert(customer);

        // Assert
        verify(customerEntityMapper).toCustomerEntity(customer);
        verify(customerRepository).save(customerEntity);
    }

    @Test
    void shouldConvertCustomerToEntityBeforeSave() {
        // Arrange
        Customer customer = new Customer();
        customer.setName("Jane");
        customer.setCpf("98765432100");

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName("Jane");
        customerEntity.setCpf("98765432100");

        when(customerEntityMapper.toCustomerEntity(customer)).thenReturn(customerEntity);

        // Act
        insertCustomerAdapter.insert(customer);

        // Assert
        var inOrder = inOrder(customerEntityMapper, customerRepository);
        inOrder.verify(customerEntityMapper).toCustomerEntity(customer);
        inOrder.verify(customerRepository).save(customerEntity);
    }
}
