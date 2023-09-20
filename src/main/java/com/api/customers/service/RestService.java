package com.api.customers.service;

import com.api.customers.model.AccountBalance;
import com.api.customers.model.TransactionResponseBody;

public interface RestService {

    TransactionResponseBody callTransactionService(Long customerId);

    AccountBalance callAccountService(Long customerId);

}
