package com.gustavosantos.ecommerce_api.controllers;

import com.gustavosantos.ecommerce_api.dto.CustomerListDTO;
import com.gustavosantos.ecommerce_api.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerListDTO>> findByFirstOrLastName(@RequestParam("name") String name) {
        List<CustomerListDTO> customerListDTO = customerService.findByFirstNameOrLastName(name);
        return ResponseEntity.ok(customerListDTO);
    }
}
