package com.marcusantosproject.backend.dto.address;

import jakarta.validation.constraints.*;

public record AddressRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must have at most 100 characters")
        String name,

        @NotBlank(message = "Street is required")
        @Size(max = 150, message = "Street must have at most 150 characters")
        String street,

        @NotNull(message = "Number is required")
        @PositiveOrZero(message = "Number must be zero or positive")
        Integer number,

        @Size(max = 255, message = "Complement must have at most 255 characters")
        String complement,

        @NotBlank(message = "Neighborhood is required")
        @Size(max = 100, message = "Neighborhood must have at most 100 characters")
        String neighborhood,

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must have at most 100 characters")
        String city,

        @NotBlank(message = "State is required")
        @Size(max = 100, message = "State must have at most 100 characters")
        String state,

        @NotBlank(message = "Zipcode is required")
        @Pattern(
                regexp = "\\d{5}-?\\d{3}",
                message = "Invalid zipcode"
        )
        String zipcode) {
}