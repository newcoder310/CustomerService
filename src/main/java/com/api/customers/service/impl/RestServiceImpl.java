package com.api.customers.service.impl;

import com.api.customers.client.RestClient;
import com.api.customers.client.config.RestClientConfig;
import com.api.customers.exception.ExternalCallFailedException;
import com.api.customers.model.AccountBalance;
import com.api.customers.model.TransactionResponseBody;
import com.api.customers.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestServiceImpl implements RestService {

    private final RestClient restClient;

    private final RestClientConfig restClientConfig;

    @Override
    public TransactionResponseBody callTransactionService(Long customerId) {
        try {
            return (TransactionResponseBody) restClient.call(customerId, restClientConfig.getTransactionServiceHostPort()+"/transaction/retrieveTransactionsByCustomerId/", TransactionResponseBody.class);
        } catch (Exception exception) {
            throw new ExternalCallFailedException("The external call to transactions service failed");
        }

    }

    @Override
    public AccountBalance callAccountService(Long customerId) {
        try {
            Class<AccountBalance> accountBalanceClass = AccountBalance.class;
            return (AccountBalance) restClient.call(customerId, restClientConfig.getAccountServiceHostPort()+"/account/retrieveAccountBalanceByCustomerId/", AccountBalance.class);
        } catch (Exception exception) {
            throw new ExternalCallFailedException("The external call to accounts service failed");
        }
    }
}
