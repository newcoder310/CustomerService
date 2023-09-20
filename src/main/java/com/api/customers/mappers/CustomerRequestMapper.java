package com.api.customers.mappers;

import com.api.customers.model.Customer;
import com.api.customers.model.CustomerRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerRequestMapper {

    Customer toCustomer(CustomerRequestBody customerResponseMapper);
}
