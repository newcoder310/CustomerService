package com.api.customers.service;

import com.api.customers.model.CustomerDetailedResponseBody;
import com.api.customers.model.CustomerRequestBody;
import com.api.customers.model.CustomerResponseBody;

public interface CustomerService {

    CustomerResponseBody retrieveCustomerById(Long customerId);

    CustomerDetailedResponseBody retrieveDetailedCustomerInformation(Long customerId);

    CustomerResponseBody createNewCustomer(CustomerRequestBody customerRequestBody);

}
