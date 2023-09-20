package com.api.customers.mappers;

import com.api.customers.model.AccountBalance;
import com.api.customers.model.Customer;
import com.api.customers.model.CustomerDetailedResponseBody;
import com.api.customers.model.TransactionResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CustomerDetailedResponseMapperTest {

    private CustomerDetailedResponseMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new CustomerDetailedResponseMapperImpl();
    }

    @Test
    void toCustomerResponse_ValidInput_ReturnsExpectedResult() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("John");
        customer.setSurname("Doe");
        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setBalance("100.0");
        TransactionResponseBody transactionResponseBody = new TransactionResponseBody();

        CustomerDetailedResponseBody result = mapper.toCustomerResponse(customer, accountBalance, transactionResponseBody);

        assertNotNull(result);
        assertEquals(1L, result.getCustomerId());
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
        assertEquals(transactionResponseBody, result.getTransactionResponseBody());
    }
}

