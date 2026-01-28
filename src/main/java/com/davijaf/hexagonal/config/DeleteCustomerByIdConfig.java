package com.davijaf.hexagonal.config;

import com.davijaf.hexagonal.application.core.usecase.DeleteCustomerByIdUseCase;
import com.davijaf.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.davijaf.hexagonal.application.ports.out.DeleteCustomerByIdOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteCustomerByIdConfig {

    @Bean
    public DeleteCustomerByIdUseCase deleteCustomerByIdUseCase(
            FindCustomerByIdInputPort findCustomerByIdInputPort,
            DeleteCustomerByIdOutputPort deleteCustomerByIdOutputPort) {
        return new DeleteCustomerByIdUseCase(findCustomerByIdInputPort, deleteCustomerByIdOutputPort);
    }
}
