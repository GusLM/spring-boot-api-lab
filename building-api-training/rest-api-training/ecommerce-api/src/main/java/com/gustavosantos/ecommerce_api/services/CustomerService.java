package com.gustavosantos.ecommerce_api.services;

import com.gustavosantos.ecommerce_api.dto.CustomerListDTO;
import com.gustavosantos.ecommerce_api.model.Customer;
import com.gustavosantos.ecommerce_api.repositories.CustomerRepository;
import com.gustavosantos.ecommerce_api.services.exceptions.ResourceNotFoundException;
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

    public Page<CustomerListDTO> findByFirstNameOrLastName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.findByFirstNameOrLastName(name, pageable);
        if (customerPage.isEmpty()) {
            throw new ResourceNotFoundException("Customer not found");
        }
        return customerPage.map(this::toListDTO);
    }

    public Page<CustomerListDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerPage.map(this::toListDTO);
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
