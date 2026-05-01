package com.gustavosantos.ecommerce_api.services;

import com.gustavosantos.ecommerce_api.dto.customers.CustomerCreateDTO;
import com.gustavosantos.ecommerce_api.dto.customers.CustomerDetailDTO;
import com.gustavosantos.ecommerce_api.dto.customers.CustomerListDTO;
import com.gustavosantos.ecommerce_api.dto.customers.CustomerUpdateDTO;
import com.gustavosantos.ecommerce_api.mappers.customers.CustomerMapper;
import com.gustavosantos.ecommerce_api.model.Customer;
import com.gustavosantos.ecommerce_api.repositories.CustomerRepository;
import com.gustavosantos.ecommerce_api.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public Page<CustomerListDTO> findCustomers(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (name == null || name.isBlank()) {
            return customerRepository.findAllProjected(pageable);
        }

        return customerRepository.findByFirstNameOrLastName(name, pageable);
    }

    public CustomerDetailDTO findCustomerDetail(UUID publicId) {
        Customer obj = customerRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return customerMapper.toDetailDTO(obj);
    }

    public void updateCustomer(UUID publicId, CustomerUpdateDTO dto) {
        Customer obj = customerRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        customerMapper.updateEntity(obj, dto);
        obj.setUpdatedAt();
        customerRepository.save(obj);
    }

    public void deleteCustomer(UUID publicId) {
        Customer obj = customerRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        customerRepository.delete(obj);
    }

    @Transactional
    public CustomerDetailDTO insertCustomer(CustomerCreateDTO dto) {
        Customer obj = customerMapper.toEntity(dto);
        obj.setCreatedAt();
        customerRepository.save(obj);
        return customerMapper.toDetailDTO(obj);
    }
}
