package com.api.customers.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class RestClient {

    public Object call(Long customerId, String uri, Class<?> object) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri + customerId, object);
    }
}
