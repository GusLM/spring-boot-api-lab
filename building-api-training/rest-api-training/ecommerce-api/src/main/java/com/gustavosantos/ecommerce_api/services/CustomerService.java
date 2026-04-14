package com.gustavosantos.ecommerce_api.services;

import com.gustavosantos.ecommerce_api.dto.CustomerListDTO;
import com.gustavosantos.ecommerce_api.model.Customer;
import com.gustavosantos.ecommerce_api.repositories.CustomerRepository;
import com.gustavosantos.ecommerce_api.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerListDTO> findByFirstNameOrLastName(String name) {
        List<Customer> customers = customerRepository.findByFirstNameOrLastName(name);
        if (customers == null || customers.isEmpty()) {
            throw new ResourceNotFoundException("Customer not found");
        }
        return customers.stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
    }

    private CustomerListDTO toListDTO(Customer customer) {
        CustomerListDTO customerListDTO = new CustomerListDTO();
        customerListDTO.setPublicId(customer.getPublicId());
        customerListDTO.setFirstName(customer.getFirstName());
        customerListDTO.setLastName(customer.getLastName());
        customerListDTO.setEmail(customer.getEmail());
        return customerListDTO;
    }
}
