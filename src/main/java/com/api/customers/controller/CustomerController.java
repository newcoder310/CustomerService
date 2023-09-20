package com.api.customers.controller;


import com.api.customers.model.CustomerDetailedResponseBody;
import com.api.customers.model.CustomerRequestBody;
import com.api.customers.model.CustomerResponseBody;
import com.api.customers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/customer/{customerID}/getCustomerInformation")
    public ResponseEntity<CustomerResponseBody> getCustomerById(@PathVariable("customerID") Long customerID) {
        return new ResponseEntity<>(customerService.retrieveCustomerById(customerID), HttpStatus.OK);
    }

    @GetMapping("/customer/{customerID}/getDetailedCustomerInformation")
    public ResponseEntity<CustomerDetailedResponseBody> getCustomerDetailedResponse(@PathVariable("customerID") Long customerID) {
        return new ResponseEntity<>(customerService.retrieveDetailedCustomerInformation(customerID), HttpStatus.OK);
    }

    @PostMapping("/customer/createCustomer")
    public ResponseEntity<CustomerResponseBody> createCustomer(@Valid @RequestBody CustomerRequestBody customerRequestBody) {
        return new ResponseEntity<>(customerService.createNewCustomer(customerRequestBody), HttpStatus.OK);
    }

}
