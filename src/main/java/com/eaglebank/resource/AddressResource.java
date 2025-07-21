package com.eaglebank.resource;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AddressResource(
        @NotBlank String line1,
        String line2,
        String line3,
        @NotBlank String town,
        @NotBlank String county,
        @NotBlank String postcode) {
}
