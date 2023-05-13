package com.github.awcz.greening.errors;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
final class ErrorResponse {

    private final String errorMessage;

    public ErrorResponse(Exception exception) {
        this.errorMessage = exception.getMessage();
    }

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
