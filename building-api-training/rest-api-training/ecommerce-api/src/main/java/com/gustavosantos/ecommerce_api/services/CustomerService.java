package com.gustavosantos.ecommerce_api.services;

import com.gustavosantos.ecommerce_api.dto.customers.CustomerListDTO;
import com.gustavosantos.ecommerce_api.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Page<CustomerListDTO> findCustomers(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (name == null || name.isBlank()) {
            return customerRepository.findAllProjected(pageable);
        }

        return customerRepository.findByFirstNameOrLastName(name, pageable);
    }
}
