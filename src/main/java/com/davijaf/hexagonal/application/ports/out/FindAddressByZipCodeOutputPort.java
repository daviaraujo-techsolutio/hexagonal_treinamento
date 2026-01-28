package com.davijaf.hexagonal.application.ports.out;

import com.davijaf.hexagonal.application.core.domain.Address;

public interface FindAddressByZipCodeOutputPort {

    Address find(String zipCode);
}
