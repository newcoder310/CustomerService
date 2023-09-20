package com.api.customers.mappers;

import com.api.customers.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerDetailedResponseMapper {

    @Mapping(target = "customerId", source = "customer.customerId")
    @Mapping(target = "name", source = "customer.name")
    @Mapping(target = "surname", source = "customer.surname")
    @Mapping(target = "balance", source = "accountBalance.balance")
    @Mapping(target = "transactionResponseBody", source = "transactionResponseBody")
    CustomerDetailedResponseBody toCustomerResponse(Customer customer, AccountBalance accountBalance, TransactionResponseBody transactionResponseBody);

    TransactionResponse toTransactionResponse(TransactionResponse source);
}
