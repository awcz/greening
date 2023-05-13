package com.github.awcz.greening.errors;

import com.fasterxml.jackson.core.JsonParseException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.web.router.exceptions.UnsatisfiedBodyRouteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ValidationException;

@Controller
final class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @io.micronaut.http.annotation.Error(global = true)
    HttpResponse<ErrorResponse> handleException(Throwable throwable) {
        if (throwable instanceof ValidationException exception) {
            return HttpResponse.badRequest(new ErrorResponse(exception));
        }
        if (throwable instanceof RequestValidationException exception) {
            return HttpResponse.badRequest(new ErrorResponse(exception));
        }
        if (throwable instanceof UnsatisfiedBodyRouteException exception) {
            return HttpResponse.badRequest(new ErrorResponse(exception));
        }
        if (throwable instanceof JsonParseException exception) {
            return HttpResponse.badRequest(new ErrorResponse(exception));
        }
        var message = "Error occurred";
        LOGGER.error(message, throwable);
        return HttpResponse.serverError(new ErrorResponse(message));
    }
}
