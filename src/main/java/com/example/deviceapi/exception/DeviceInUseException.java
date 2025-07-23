
package com.example.deviceapi.exception;

import org.zalando.problem.Status;

import java.net.URI;

/**
 * Thrown when an operation is invalid on an in-use device.
 */
public class DeviceInUseException extends AbstractCustomThrowableProblem {
     private static final URI TYPE = URI.create("urn:device-api:problem-type:invalid_username");

    /**
     * Creates new instance of {@code DeviceInUseException}.
     */
    public DeviceInUseException() {
        super(TYPE, "Device In Use", Status.CONFLICT,
                "Device is in use and cannot be fully updated", null);
    }

    /**
     * Instantiates a new Device in use exception.
     *
     * @param message the message
     */
    public DeviceInUseException(final String message) {
        super(TYPE, "Device In Use", Status.CONFLICT,
                message, null);
    }
}
