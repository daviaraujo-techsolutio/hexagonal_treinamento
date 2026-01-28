package com.davijaf.hexagonal.application.ports.out;

import com.davijaf.hexagonal.application.core.domain.Customer;

public interface UpdateCustomerOutputPort {

    void update(Customer customer);
}
