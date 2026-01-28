package com.davijaf.hexagonal.config;

import com.davijaf.hexagonal.application.core.usecase.UpdateCustomerUseCase;
import com.davijaf.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.davijaf.hexagonal.application.ports.out.FindAddressByZipCodeOutputPort;
import com.davijaf.hexagonal.application.ports.out.UpdateCustomerOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateCustomerConfig {

    @Bean
    public UpdateCustomerUseCase updateCustomerUseCase(
            FindCustomerByIdInputPort findCustomerByIdInputPort,
            FindAddressByZipCodeOutputPort findAddressByZipCodeOutputPort,
            UpdateCustomerOutputPort updateCustomerOutputPort) {
        return new UpdateCustomerUseCase(
                findCustomerByIdInputPort,
                findAddressByZipCodeOutputPort,
                updateCustomerOutputPort);
    }
}
