package com.gustavosantos.ecommerce_api.services;

import com.gustavosantos.ecommerce_api.model.Customer;
import com.gustavosantos.ecommerce_api.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
