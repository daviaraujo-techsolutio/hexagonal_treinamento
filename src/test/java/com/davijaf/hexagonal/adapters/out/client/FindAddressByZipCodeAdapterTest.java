package com.davijaf.hexagonal.adapters.out.client;

import com.davijaf.hexagonal.adapters.out.client.mapper.AddressResponseMapper;
import com.davijaf.hexagonal.adapters.out.client.response.AddressResponse;
import com.davijaf.hexagonal.application.core.domain.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAddressByZipCodeAdapterTest {

    @Mock
    private FindAddressByZipCodeClient findAddressByZipCodeClient;

    @Mock
    private AddressResponseMapper addressResponseMapper;

    @InjectMocks
    private FindAddressByZipCodeAdapter findAddressByZipCodeAdapter;

    private String zipCode;
    private AddressResponse addressResponse;
    private Address address;

    @BeforeEach
    void setUp() {
        zipCode = "01310-100";

        addressResponse = new AddressResponse();
        addressResponse.setStreet("Avenida Paulista");
        addressResponse.setCity("Sao Paulo");
        addressResponse.setState("SP");

        address = new Address();
        address.setStreet("Avenida Paulista");
        address.setCity("Sao Paulo");
        address.setState("SP");
    }

    @Test
    void shouldFindAddressByZipCode() {
        when(findAddressByZipCodeClient.find(zipCode)).thenReturn(addressResponse);
        when(addressResponseMapper.toAddress(any(AddressResponse.class))).thenReturn(address);

        Address result = findAddressByZipCodeAdapter.find(zipCode);

        assertNotNull(result);
        assertEquals("Avenida Paulista", result.getStreet());
        assertEquals("Sao Paulo", result.getCity());
        assertEquals("SP", result.getState());

        verify(findAddressByZipCodeClient, times(1)).find(zipCode);
        verify(addressResponseMapper, times(1)).toAddress(addressResponse);
    }

    @Test
    void shouldFindAddressWithDifferentZipCode() {
        String anotherZipCode = "22041-080";

        AddressResponse anotherResponse = new AddressResponse();
        anotherResponse.setStreet("Avenida Atlantica");
        anotherResponse.setCity("Rio de Janeiro");
        anotherResponse.setState("RJ");

        Address anotherAddress = new Address();
        anotherAddress.setStreet("Avenida Atlantica");
        anotherAddress.setCity("Rio de Janeiro");
        anotherAddress.setState("RJ");

        when(findAddressByZipCodeClient.find(anotherZipCode)).thenReturn(anotherResponse);
        when(addressResponseMapper.toAddress(anotherResponse)).thenReturn(anotherAddress);

        Address result = findAddressByZipCodeAdapter.find(anotherZipCode);

        assertNotNull(result);
        assertEquals("Avenida Atlantica", result.getStreet());
        assertEquals("Rio de Janeiro", result.getCity());
        assertEquals("RJ", result.getState());
    }
}
