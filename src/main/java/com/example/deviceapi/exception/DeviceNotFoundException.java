
package com.example.deviceapi.exception;

import org.zalando.problem.Status;

import java.net.URI;

/**
 * Thrown when a device is not found by ID.
 */
public class DeviceNotFoundException extends AbstractCustomThrowableProblem {


    private static final URI TYPE = URI.create("urn:device-service:problem-type:invalid_username");

    /**
     * Creates new instance of {@code DeviceNotFoundException}.
     */
    public DeviceNotFoundException() {
        super(TYPE, "Device not found", Status.NOT_FOUND, "Device not found", null);
    }
}
