package com.gustavosantos.ecommerce_api.mappers.customers;

import com.gustavosantos.ecommerce_api.dto.customers.CustomerCreateDTO;
import com.gustavosantos.ecommerce_api.dto.customers.CustomerDetailDTO;
import com.gustavosantos.ecommerce_api.dto.customers.CustomerUpdateDTO;
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

    public void updateEntity(Customer customer, CustomerUpdateDTO dto) {
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setStreet(dto.getStreet());
        customer.setNumber(dto.getNumber());
        customer.setNeighborhood(dto.getNeighborhood());
        customer.setCity(dto.getCity());
        customer.setState(dto.getState());
        customer.setPostalCode(dto.getPostalCode());
        customer.setCountry(dto.getCountry());
    }

    public Customer toEntity(CustomerCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setTaxId(dto.getTaxId());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setStreet(dto.getStreet());
        customer.setNumber(dto.getNumber());
        customer.setNeighborhood(dto.getNeighborhood());
        customer.setCity(dto.getCity());
        customer.setState(dto.getState());
        customer.setPostalCode(dto.getPostalCode());
        customer.setCountry(dto.getCountry());

        return customer;
    }
}
