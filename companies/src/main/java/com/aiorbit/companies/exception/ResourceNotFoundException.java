package com.aiorbit.companies.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceType, String field, Object value) {
        super(String.format("%s not found with %s: '%s'", resourceType, field, value));
    }
}