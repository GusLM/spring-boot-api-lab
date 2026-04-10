package com.gustavosantos.ecommerce_api.model;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "uuid"})
@ToString(exclude = {"id", "uuid"})
public class costumer {

    @Getter(AccessLevel.PUBLIC)
    private Long id;

    @Getter(AccessLevel.PUBLIC)
    private UUID uuid;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String taxId;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String street;

    @Getter
    @Setter
    private String number;

    @Getter
    @Setter
    private String neighborhood;

    @Getter
    @Setter
    private String city;

    @Getter
    @Setter
    private String state;

    @Getter
    @Setter
    private String postalCode;

    public costumer(
            String firstName,
            String lastName,
            String taxId,
            String phone,
            String email,
            String street,
            String number,
            String neighborhood,
            String city,
            String state,
            String postalCode
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.taxId = taxId;
        this.phone = phone;
        this.email = email;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }
}
