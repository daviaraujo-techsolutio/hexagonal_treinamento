package com.davijaf.hexagonal.config;

import com.davijaf.hexagonal.application.core.usecase.FindCustomerByIdUseCase;
import com.davijaf.hexagonal.application.ports.out.FindCustomerByIdOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FindCustomerByIdConfig {

    @Bean
    public FindCustomerByIdUseCase findCustomerByIdUseCase(
            FindCustomerByIdOutputPort findCustomerByIdOutputPort) {
        return new FindCustomerByIdUseCase(findCustomerByIdOutputPort);
    }
}
