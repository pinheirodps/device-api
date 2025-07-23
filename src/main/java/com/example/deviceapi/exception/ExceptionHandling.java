package com.example.deviceapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import java.net.URI;

/**
 * The type Exception handling.
 */
@ControllerAdvice()
@Slf4j
public class ExceptionHandling implements ProblemHandling {

    @Override
    public ResponseEntity<Problem> handleThrowable(final Throwable throwable, final NativeWebRequest request) {
        log.error(throwable.getMessage(), throwable);

        return this.create(throwable, request);
    }

    @Override
    public ResponseEntity<Problem> handleProblem(ThrowableProblem problem, final NativeWebRequest request) {

        if (problem.getStatus() == null || Status.NOT_FOUND.getStatusCode() != problem.getStatus().getStatusCode()) {
            log.error(problem.toString(), problem);
        }

        return this.create(problem, request);
    }


    @Override
    public URI defaultConstraintViolationType() {
        return URI.create("urn:device-api:problem-type:validation_constraint");
    }

    @Override
    public boolean isCausalChainsEnabled() {
        return true;
    }

    /**
     * Handle no resource found exception response entity.
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<Problem> handleNoResourceFoundException(final NoResourceFoundException exception, final NativeWebRequest request) {
        log.warn("No resource found for path: {}", exception.getResourcePath());
        return create(Problem.builder()
                        .withStatus(Status.NOT_FOUND)
                        .withTitle("Not Found")
                        .withDetail("The requested resource was not found on this server.")
                        .withInstance(URI.create(request.getNativeRequest(HttpServletRequest.class).getRequestURI()))
                        .build(),
                request);
    }


}
