package com.api.customers;

import com.api.customers.client.RestClient;
import com.api.customers.exception.CustomerNotPresentException;
import com.api.customers.model.*;
import com.api.customers.repository.CustomerDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerApplicationTests {

    private final static String CUSTOMER_DETAILS = "/customer/123456/getCustomerInformation";
    public static final String ACCOUNT_SERVICE_URI = "http://localhost:8080/account/retrieveAccountBalanceByCustomerId/";
    public static final String TRANSACTION_SERVICE_URI = "http://localhost:8080/transaction/retrieveTransactionsByCustomerId/";
    public static final String CUSTOMER_DETAILED_INFORMATION_URI = "/customer/123456/getDetailedCustomerInformation";
    public static final long CUSTOMER_ID = 123456;
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String BALANCE = "40.0";
    public static final String APPLICATION_JSON = "application/json";
    public static final String BASIC_CUSTOMER_INFO_URI = "/customer/123456/getCustomerInformation";
    public static final String CREATE_CUSTOMER_URI = "/customer/createCustomer";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerDAO customerDAO;

    @MockBean
    private RestClient restClient;


    //Testing Customer Detailed Information Endpoint Test Cases BEGIN
    @Test
    void testCustomerAccountDetailedInformationTestsSuccessCase_expect_200() throws Exception {


        Customer customer = new Customer();
        customer.setCustomerId(CUSTOMER_ID);
        customer.setName(FIRST_NAME);
        customer.setName(LAST_NAME);

        when(customerDAO.getCustomerById(CUSTOMER_ID)).thenReturn(customer);
        AccountBalance balance = new AccountBalance();
        balance.setBalance(BALANCE);

        TransactionResponseBody transactionResponseBody = new TransactionResponseBody();
        TransactionResponse transactionResponse1 = new TransactionResponse();
        transactionResponse1.setAccountId(1234l);
        transactionResponse1.setCustomerId(CUSTOMER_ID);
        transactionResponse1.setAmount("10.0");
        transactionResponse1.setDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        TransactionResponse transactionResponse2 = new TransactionResponse();
        transactionResponse2.setAccountId(1234l);
        transactionResponse2.setCustomerId(CUSTOMER_ID);
        transactionResponse2.setAmount("10.0");
        transactionResponse2.setDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        transactionResponseBody.setTransactions(Arrays.asList(transactionResponse1, transactionResponse2));

        when(restClient.call(CUSTOMER_ID, ACCOUNT_SERVICE_URI, AccountBalance.class)).thenReturn(balance);
        when(restClient.call(CUSTOMER_ID, TRANSACTION_SERVICE_URI, TransactionResponseBody.class)).thenReturn(transactionResponseBody);

        mockMvc.perform(get(CUSTOMER_DETAILED_INFORMATION_URI).contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testCustomerAccountDetailsCustomerNotPresentCase_expect_400() throws Exception {
        mockMvc.perform(get("/customer/12345/getDetailedCustomerInformation").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Customer is not present")));
    }

    @Test
    void testCustomerAccountDetailsCustomerIdInvalidString_expect_400() throws Exception {
        mockMvc.perform(get("/customer/test/getDetailedCustomerInformation").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Invalid Field Value:")));
    }


    @Test
    void testCustomerAccountDetailsWithClientExceptionForAccounts_expect_400() throws Exception {

        Customer customer = new Customer();
        customer.setCustomerId(CUSTOMER_ID);
        customer.setName(FIRST_NAME);
        customer.setName(LAST_NAME);

        when(customerDAO.getCustomerById(CUSTOMER_ID)).thenReturn(customer);
        when(restClient.call(CUSTOMER_ID, ACCOUNT_SERVICE_URI, AccountBalance.class)).thenThrow(new RuntimeException());
        mockMvc.perform(get(CUSTOMER_DETAILED_INFORMATION_URI).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("The external call to accounts service failed")));
    }

    @Test
    void testCustomerAccountDetailsWithClientExceptionForTransactions_expect_400() throws Exception {

        Customer customer = new Customer();
        customer.setCustomerId(CUSTOMER_ID);
        customer.setName(FIRST_NAME);
        customer.setName(LAST_NAME);

        when(customerDAO.getCustomerById(CUSTOMER_ID)).thenReturn(customer);

        AccountBalance balance = new AccountBalance();
        balance.setBalance(BALANCE);

        when(restClient.call(CUSTOMER_ID, ACCOUNT_SERVICE_URI, AccountBalance.class)).thenReturn(balance);

        when(restClient.call(CUSTOMER_ID, TRANSACTION_SERVICE_URI, TransactionResponseBody.class)).thenThrow(new RuntimeException());
        mockMvc.perform(get(CUSTOMER_DETAILED_INFORMATION_URI).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("The external call to transactions service failed")));
    }

    //Testing Customer Detailed Information Endpoint Test Cases END


    //Testing Customer BASIC Information Endpoint Test Cases START

    @Test
    void testCustomerInformationSuccess_expect_200() throws Exception {
        mockMvc.perform(get(BASIC_CUSTOMER_INFO_URI).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Customer is not present")));
    }

    //Testing Customer BASIC Information Endpoint Test Cases END

    //Testing Customer Creation START
    @Test
    void testCreateCustomerSuccess_expect_200() throws Exception {
        CustomerRequestBody customerRequestBody = new CustomerRequestBody();
        customerRequestBody.setName("John");
        customerRequestBody.setSurname("Doe");

        Customer customer = new Customer();
        customer.setCustomerId(CUSTOMER_ID);
        customer.setName(FIRST_NAME);
        customer.setSurname(LAST_NAME);

        when(customerDAO.createNewCustomer(any())).thenReturn(customer);
        mockMvc.perform(post(CREATE_CUSTOMER_URI).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John")));
    }

    @Test
    void testCreateCustomerMissingName_expect_400() throws Exception {
        CustomerRequestBody customerRequestBody = new CustomerRequestBody();
        customerRequestBody.setSurname("Doe");

        Customer customer = new Customer();
        customer.setCustomerId(CUSTOMER_ID);
        customer.setName(FIRST_NAME);
        customer.setSurname(LAST_NAME);

        when(customerDAO.createNewCustomer(any())).thenReturn(customer);
        mockMvc.perform(post(CREATE_CUSTOMER_URI).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Request Validation Failed")));
    }
    @Test
    void testCreateCustomerMissingSurname_expect_400() throws Exception {
        CustomerRequestBody customerRequestBody = new CustomerRequestBody();
        customerRequestBody.setName("Doe");

        Customer customer = new Customer();
        customer.setCustomerId(CUSTOMER_ID);
        customer.setName(FIRST_NAME);
        customer.setSurname(LAST_NAME);

        when(customerDAO.createNewCustomer(any())).thenReturn(customer);
        mockMvc.perform(post(CREATE_CUSTOMER_URI).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Request Validation Failed")));
    }

    //Testing Customer Creation END


    //Testing Get Customer Informaiton BEGIN
    @Test
    void testGetCustomerInformationSuccess_expect_200() throws Exception {
        CustomerRequestBody customerRequestBody = new CustomerRequestBody();
        customerRequestBody.setSurname("Doe");

        Customer customer = new Customer();
        customer.setCustomerId(CUSTOMER_ID);
        customer.setName(FIRST_NAME);
        customer.setSurname(LAST_NAME);

        when(customerDAO.getCustomerById(CUSTOMER_ID)).thenReturn(customer);
        mockMvc.perform(get(CUSTOMER_DETAILS).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId", is(123456)));
    }


    @Test
    void testGetCustomerInformationNotPresent_expect_400() throws Exception {
        CustomerRequestBody customerRequestBody = new CustomerRequestBody();
        customerRequestBody.setName("John");
        customerRequestBody.setSurname("Doe");

        Customer customer = new Customer();
        customer.setCustomerId(CUSTOMER_ID);
        customer.setName(FIRST_NAME);
        customer.setSurname(LAST_NAME);

        when(customerDAO.getCustomerById(CUSTOMER_ID)).thenThrow(new CustomerNotPresentException("Customer does not exit"));
        mockMvc.perform(get(CUSTOMER_DETAILS).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Customer does not exit")));
    }



    //Testing Get Customer Informaiton END

}
