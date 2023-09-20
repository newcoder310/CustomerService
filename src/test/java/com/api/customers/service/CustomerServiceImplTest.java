package com.api.customers.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.api.customers.exception.CustomerNotPresentException;
import com.api.customers.mappers.CustomerDetailedResponseMapper;
import com.api.customers.mappers.CustomerRequestMapper;
import com.api.customers.mappers.CustomerResponseMapper;
import com.api.customers.model.*;
import com.api.customers.repository.CustomerDAO;
import com.api.customers.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private CustomerResponseMapper customerResponseMapper;

    @Mock
    private CustomerDetailedResponseMapper customerDetailedResponseMapper;

    @Mock
    private RestService restService;

    @Mock
    private CustomerRequestMapper customerRequestMapper;

    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;

    @Test
    void retrieveDetailedCustomerInformation_shouldReturnDetailedCustomerInformation() {
        // Arrange
        Long customerID = 1L;
        Customer customer = new Customer();
        AccountBalance accountBalance = new AccountBalance();
        TransactionResponseBody transactionResponseBody = new TransactionResponseBody();
        CustomerDetailedResponseBody expected = new CustomerDetailedResponseBody();
        when(customerDAO.getCustomerById(customerID)).thenReturn(customer);
        when(restService.callAccountService(customerID)).thenReturn(accountBalance);
        when(restService.callTransactionService(customerID)).thenReturn(transactionResponseBody);
        when(customerDetailedResponseMapper.toCustomerResponse(customer, accountBalance, transactionResponseBody)).thenReturn(expected);

        // Act
        CustomerDetailedResponseBody actual = customerServiceImpl.retrieveDetailedCustomerInformation(customerID);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void retrieveDetailedCustomerInformation_shouldThrowExceptionIfCustomerNotPresent() {
        // Arrange
        Long customerID = 1L;
        when(customerDAO.getCustomerById(customerID)).thenReturn(null);

        // Act & Assert
        assertThrows(CustomerNotPresentException.class, () -> customerServiceImpl.retrieveDetailedCustomerInformation(customerID));
    }

    @Test
    void createNewCustomer_shouldReturnCreatedCustomerInformation() {
        // Arrange
        CustomerRequestBody customerRequestBody = new CustomerRequestBody();
        Customer customer = new Customer();
        CustomerResponseBody expected = new CustomerResponseBody();
        when(customerRequestMapper.toCustomer(customerRequestBody)).thenReturn(customer);
        when(customerDAO.createNewCustomer(customer)).thenReturn(customer);
        when(customerResponseMapper.toCustomerResponse(customer)).thenReturn(expected);

        // Act
        CustomerResponseBody actual = customerServiceImpl.createNewCustomer(customerRequestBody);

        // Assert
        assertEquals(expected, actual);
    }
}
