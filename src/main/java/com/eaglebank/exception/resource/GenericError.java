package com.eaglebank.exception.resource;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

public record GenericError(
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL) List<Map<String, String>> details) {
}
