package com.davijaf.hexagonal.application.ports.in;

import com.davijaf.hexagonal.application.core.domain.Customer;

public interface FindCustomerByIdInputPort {

    Customer find(String id);
}
