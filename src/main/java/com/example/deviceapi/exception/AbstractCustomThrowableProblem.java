package com.example.deviceapi.exception;

import lombok.Data;
import lombok.ToString;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.StatusType;

import java.net.URI;
import java.util.Map;

@Data
@ToString(callSuper = true)
public abstract class AbstractCustomThrowableProblem extends AbstractThrowableProblem {

    /**
     * Creates an instance of {@link AbstractCustomThrowableProblem}.
     *
     * @param type       the exception URN
     * @param title      the exception title
     * @param status     the exception status code.
     * @param detail     the exception detailed message.
     * @param parameters the exception parameter.
     */
    public AbstractCustomThrowableProblem(final URI type, final String title, final StatusType status,
                                          final String detail, final Map<String, Object> parameters) {
        super(type, title, status, detail, null, null, parameters);
    }

}
