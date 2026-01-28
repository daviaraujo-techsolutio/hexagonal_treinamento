package com.davijaf.hexagonal.config;

import com.davijaf.hexagonal.application.core.usecase.InsertCustomerUseCase;
import com.davijaf.hexagonal.application.ports.out.FindAddressByZipCodeOutputPort;
import com.davijaf.hexagonal.application.ports.out.InsertCustomerOutputPort;
import com.davijaf.hexagonal.application.ports.out.SendCpfForValidationOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InsertCustomerConfig {

    @Bean
    public InsertCustomerUseCase insertCustomerUseCase(
            FindAddressByZipCodeOutputPort findAddressByZipCodeOutputPort,
            InsertCustomerOutputPort insertCustomerOutputPort,
            SendCpfForValidationOutputPort sendCpfForValidationOutputPort) {
        return new InsertCustomerUseCase(
                findAddressByZipCodeOutputPort,
                insertCustomerOutputPort,
                sendCpfForValidationOutputPort);
    }
}
