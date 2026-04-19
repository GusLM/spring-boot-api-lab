package com.gustavosantos.ecommerce_api.controllers;

import com.gustavosantos.ecommerce_api.dto.CustomerListDTO;
import com.gustavosantos.ecommerce_api.dto.PageResponse;
import com.gustavosantos.ecommerce_api.services.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public ResponseEntity<PageResponse<CustomerListDTO>> findCustomers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<CustomerListDTO> customerListDTO = customerService.findCustomers(name, page, size);
        return ResponseEntity.ok().body(PageResponse.from(customerListDTO));
    }
}
