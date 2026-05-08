package com.gustavosantos.ecommerce_api.controllers;

import com.gustavosantos.ecommerce_api.dto.customers.CustomerCreateDTO;
import com.gustavosantos.ecommerce_api.dto.customers.CustomerDetailDTO;
import com.gustavosantos.ecommerce_api.dto.customers.CustomerListDTO;
import com.gustavosantos.ecommerce_api.dto.PageResponse;
import com.gustavosantos.ecommerce_api.dto.customers.CustomerUpdateDTO;
import com.gustavosantos.ecommerce_api.dto.orders.OrderListDTO;
import com.gustavosantos.ecommerce_api.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public ResponseEntity<PageResponse<CustomerListDTO>> findCustomers(
            @RequestParam(value = "name", required = false)
            String name,

            @RequestParam(value = "page", defaultValue = "0")
            @Min(value = 0, message = "Page must be zero or greater")
            int page,

            @RequestParam(value = "size", defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than zero")
            @Max(value = 100, message = "Size must be less than or equal to 100")
            int size
    ) {
        Page<CustomerListDTO> customerListDTO = customerService.findCustomers(name, page, size);
        return ResponseEntity.ok().body(PageResponse.from(customerListDTO));
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<CustomerDetailDTO> findCustomerDetail(@PathVariable UUID publicId) {
        CustomerDetailDTO obj = customerService.findCustomerDetail(publicId);
        return ResponseEntity.ok().body(obj);
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<Void> updateCustomer(@PathVariable UUID publicId, @RequestBody @Valid CustomerUpdateDTO dto) {
        customerService.updateCustomer(publicId, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID publicId) {
        customerService.deleteCustomer(publicId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CustomerDetailDTO> insertCustomer(@Valid @RequestBody CustomerCreateDTO dto) {
        CustomerDetailDTO obj = customerService.insertCustomer(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{publicId}")
                .buildAndExpand(obj.getPublicId())
                .toUri();

        return ResponseEntity.created(uri).body(obj);
    }

    @GetMapping("/{publicId}/orders")
    public ResponseEntity<PageResponse<OrderListDTO>> findOrdersByCustomer(
            @PathVariable UUID publicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Max(value = 100, message = "Size must be less than or equal to 100") int size
    ) {
        Page<OrderListDTO> orderListDTO = customerService.findCustomerOrders(publicId, page, size);
        return ResponseEntity.ok().body(PageResponse.from(orderListDTO));
    }
}
