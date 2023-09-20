package com.api.customers.service.impl;

import com.api.customers.exception.CustomerNotPresentException;
import com.api.customers.mappers.CustomerDetailedResponseMapper;
import com.api.customers.mappers.CustomerRequestMapper;
import com.api.customers.mappers.CustomerResponseMapper;
import com.api.customers.model.*;
import com.api.customers.repository.CustomerDAO;
import com.api.customers.service.CustomerService;
import com.api.customers.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;

    private final CustomerResponseMapper customerResponseMapper;

    private final CustomerDetailedResponseMapper customerDetailedResponseMapper;

    private final RestService restService;

    private final CustomerRequestMapper customerRequestMapper;

    @Override
    public CustomerDetailedResponseBody retrieveDetailedCustomerInformation(Long customerID) {
        Customer customer = retrieveCustomer(customerID);
        if (customer == null) {
            throw new CustomerNotPresentException("Customer is not present");
        }

        AccountBalance accountBalance = restService.callAccountService(customerID);
        TransactionResponseBody transactionResponseBody = restService.callTransactionService(customerID);

        return customerDetailedResponseMapper.toCustomerResponse(customer, accountBalance, transactionResponseBody);

    }

    @Override
    public CustomerResponseBody createNewCustomer(CustomerRequestBody customerRequestBody) {
        Customer customer = customerRequestMapper.toCustomer(customerRequestBody);
        return customerResponseMapper.toCustomerResponse(customerDAO.createNewCustomer(customer));
    }

    @Override
    public CustomerResponseBody retrieveCustomerById(Long customerID) {
        Customer customer = retrieveCustomer(customerID);
        if (customer == null) {
            throw new CustomerNotPresentException("Customer is not present");
        }

        return customerResponseMapper.toCustomerResponse(customer);

    }


    private Customer retrieveCustomer(Long customerId) {
        return customerDAO.getCustomerById(customerId);
    }

}

