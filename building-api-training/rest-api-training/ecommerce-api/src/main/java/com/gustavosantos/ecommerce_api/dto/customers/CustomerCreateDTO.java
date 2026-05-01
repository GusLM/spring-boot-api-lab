package com.gustavosantos.ecommerce_api.dto.customers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreateDTO {

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 45, message = "First name must be up to 45 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 45, message = "Last name must be up to 45 characters")
    private String lastName;

    @NotBlank(message = "Tax ID cannot be blank")
    @Size(max = 11, min = 11, message = "Tax ID must be 11 characters")
    @Pattern(regexp = "^[0-9]{11}$", message = "Tax ID must contain only numbers")
    private String taxId;

    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email must be up to 150 characters")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Size(max = 13, message = "Phone number must be up to 13 characters")
    private String phone;

    @NotBlank(message = "Street name cannot be blank")
    @Size(max = 100, message = "Street name must be up to 100 characters")
    private String street;

    @NotBlank(message = "Street number cannot be blank")
    @Size(max = 10, message = "Street number must be up to 10 characters")
    private String number;

    @NotBlank(message = "Neighborhood name cannot be blank")
    @Size(max = 50, message = "Neighborhood name must be up to 50 characters")
    private String neighborhood;

    @NotBlank(message = "City name cannot be blank")
    @Size(max = 50, message = "City name must be up to 50 characters")
    private String city;

    @NotBlank(message = "State name cannot be blank")
    @Size(max = 45, message = "State name must be up to 45 characters")
    private String state;

    @NotBlank(message = "Postal code cannot be blank")
    @Size(max = 20, message = "Postal code must be up to 20 characters")
    private String postalCode;

    @NotBlank(message = "Country name cannot be blank")
    @Size(max = 60, message = "Country name must be up to 60 characters")
    private String country;
}