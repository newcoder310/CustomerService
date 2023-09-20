package com.api.customers.repository;

import com.api.customers.exception.CustomerNotPresentException;
import com.api.customers.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomerDAO {

    private final CustomerRepository customerRepository;

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotPresentException("Customer does not exit"));
    }

    public Customer createNewCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
