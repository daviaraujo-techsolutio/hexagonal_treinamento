package com.davijaf.hexagonal.adapters.out.client.mapper;

import com.davijaf.hexagonal.adapters.out.client.response.AddressResponse;
import com.davijaf.hexagonal.application.core.domain.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressResponseMapper {

    Address toAddress(AddressResponse addressResponse);
}
