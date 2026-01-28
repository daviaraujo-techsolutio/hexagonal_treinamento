package com.davijaf.hexagonal.adapters.in.controller;

import com.davijaf.hexagonal.adapters.in.controller.handler.ApiExceptionHandler;
import com.davijaf.hexagonal.adapters.in.controller.mapper.CustomerMapper;
import com.davijaf.hexagonal.adapters.in.controller.request.CustomerRequest;
import com.davijaf.hexagonal.adapters.in.controller.response.AddressResponse;
import com.davijaf.hexagonal.adapters.in.controller.response.CustomerResponse;
import com.davijaf.hexagonal.application.core.domain.Address;
import com.davijaf.hexagonal.application.core.domain.Customer;
import com.davijaf.hexagonal.application.core.exceptions.ObjectNotFoundException;
import com.davijaf.hexagonal.application.ports.in.DeleteCustomerByIdInputPort;
import com.davijaf.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.davijaf.hexagonal.application.ports.in.InsertCustomerInputPort;
import com.davijaf.hexagonal.application.ports.in.UpdateCustomerInputPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InsertCustomerInputPort insertCustomerInputPort;

    @MockBean
    private FindCustomerByIdInputPort findCustomerByIdInputPort;

    @MockBean
    private UpdateCustomerInputPort updateCustomerInputPort;

    @MockBean
    private DeleteCustomerByIdInputPort deleteCustomerByIdInputPort;

    @MockBean
    private CustomerMapper customerMapper;

    @Test
    void shouldInsertCustomerSuccessfully() throws Exception {
        // Arrange
        CustomerRequest request = new CustomerRequest();
        request.setName("John");
        request.setCpf("12345678900");
        request.setZipCode("38400000");

        Customer customer = new Customer();
        customer.setName("John");
        customer.setCpf("12345678900");

        when(customerMapper.toCustomer(any(CustomerRequest.class))).thenReturn(customer);

        // Act & Assert
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(customerMapper).toCustomer(any(CustomerRequest.class));
        verify(insertCustomerInputPort).insert(any(Customer.class), eq("38400000"));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        // Arrange
        CustomerRequest request = new CustomerRequest();
        request.setName("");
        request.setCpf("12345678900");
        request.setZipCode("38400000");

        // Act & Assert
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(insertCustomerInputPort, never()).insert(any(), any());
    }

    @Test
    void shouldFindCustomerByIdSuccessfully() throws Exception {
        // Arrange
        String customerId = "123";
        Address address = new Address("Rua Hexagonal", "Uberlândia", "Minas Gerais");
        Customer customer = new Customer(customerId, "John", address, "12345678900", false);

        CustomerResponse response = new CustomerResponse();
        response.setName("John");
        response.setCpf("12345678900");
        response.setIsValidCpf(false);
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setStreet("Rua Hexagonal");
        addressResponse.setCity("Uberlândia");
        addressResponse.setState("Minas Gerais");
        response.setAddress(addressResponse);

        when(findCustomerByIdInputPort.find(customerId)).thenReturn(customer);
        when(customerMapper.toCustomerResponse(customer)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.cpf").value("12345678900"))
                .andExpect(jsonPath("$.isValidCpf").value(false))
                .andExpect(jsonPath("$.address.street").value("Rua Hexagonal"))
                .andExpect(jsonPath("$.address.city").value("Uberlândia"))
                .andExpect(jsonPath("$.address.state").value("Minas Gerais"));

        verify(findCustomerByIdInputPort).find(customerId);
        verify(customerMapper).toCustomerResponse(customer);
    }

    @Test
    void shouldReturnNotFoundWhenCustomerNotFound() throws Exception {
        // Arrange
        String customerId = "999";

        when(findCustomerByIdInputPort.find(customerId))
                .thenThrow(new ObjectNotFoundException(customerId));

        // Act & Assert
        mockMvc.perform(get("/api/v1/customers/{id}", customerId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Object with id 999 not found"));

        verify(findCustomerByIdInputPort).find(customerId);
    }

    @Test
    void shouldUpdateCustomerSuccessfully() throws Exception {
        // Arrange
        String customerId = "123";
        CustomerRequest request = new CustomerRequest();
        request.setName("John Updated");
        request.setCpf("12345678900");
        request.setZipCode("38400001");

        Customer customer = new Customer();
        customer.setName("John Updated");
        customer.setCpf("12345678900");

        when(customerMapper.toCustomer(any(CustomerRequest.class))).thenReturn(customer);

        // Act & Assert
        mockMvc.perform(put("/api/v1/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(customerMapper).toCustomer(any(CustomerRequest.class));
        verify(updateCustomerInputPort).update(any(Customer.class), eq("38400001"));
    }

    @Test
    void shouldDeleteCustomerSuccessfully() throws Exception {
        // Arrange
        String customerId = "123";

        // Act & Assert
        mockMvc.perform(delete("/api/v1/customers/{id}", customerId))
                .andExpect(status().isNoContent());

        verify(deleteCustomerByIdInputPort).delete(customerId);
    }

    @Test
    void shouldReturnBadRequestWhenCpfIsBlank() throws Exception {
        // Arrange
        CustomerRequest request = new CustomerRequest();
        request.setName("John");
        request.setCpf("");
        request.setZipCode("38400000");

        // Act & Assert
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenZipCodeIsBlank() throws Exception {
        // Arrange
        CustomerRequest request = new CustomerRequest();
        request.setName("John");
        request.setCpf("12345678900");
        request.setZipCode("");

        // Act & Assert
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
