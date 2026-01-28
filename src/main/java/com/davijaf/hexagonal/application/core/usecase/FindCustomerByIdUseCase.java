package com.davijaf.hexagonal.application.core.usecase;

import com.davijaf.hexagonal.application.core.domain.Customer;
import com.davijaf.hexagonal.application.core.exceptions.ObjectNotFoundException;
import com.davijaf.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.davijaf.hexagonal.application.ports.out.FindCustomerByIdOutputPort;

public class FindCustomerByIdUseCase implements FindCustomerByIdInputPort {

    private final FindCustomerByIdOutputPort findCustomerByIdOutputPort;

    public FindCustomerByIdUseCase(FindCustomerByIdOutputPort findCustomerByIdOutputPort) {
        this.findCustomerByIdOutputPort = findCustomerByIdOutputPort;
    }

    public Customer find(String id) {
        return findCustomerByIdOutputPort.find(id)
                .orElseThrow(() -> new ObjectNotFoundException(id));
    }
}
