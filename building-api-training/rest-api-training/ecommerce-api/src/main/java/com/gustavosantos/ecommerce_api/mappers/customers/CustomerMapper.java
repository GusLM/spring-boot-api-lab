package com.gustavosantos.ecommerce_api.mappers.customers;

import com.gustavosantos.ecommerce_api.dto.customers.CustomerDetailDTO;
import com.gustavosantos.ecommerce_api.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDetailDTO toDetailDTO(Customer customer) {
        return new CustomerDetailDTO(
                customer.getPublicId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getTaxId(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getStreet(),
                customer.getNumber(),
                customer.getNeighborhood(),
                customer.getCity(),
                customer.getState(),
                customer.getPostalCode(),
                customer.getCountry()
        );
    }
}
