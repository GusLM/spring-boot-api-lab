package com.gustavosantos.ecommerce_api.controllers;

import com.gustavosantos.ecommerce_api.dto.CustomerListDTO;
import com.gustavosantos.ecommerce_api.dto.PageResponse;
import com.gustavosantos.ecommerce_api.services.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<CustomerListDTO>> findByFirstOrLastName(
            @RequestParam("name") String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<CustomerListDTO> customerListDTO = customerService.findByFirstNameOrLastName(name, page, size);
        return ResponseEntity.ok().body(PageResponse.from(customerListDTO));
    }

    @GetMapping({"/findAll"})
    public ResponseEntity<PageResponse<CustomerListDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<CustomerListDTO> customerPage = customerService.findAll(page, size);
        return ResponseEntity.ok().body(PageResponse.from(customerPage));
    }
}
