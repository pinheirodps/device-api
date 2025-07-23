
package com.example.deviceapi.dto;

import com.example.deviceapi.entity.DeviceState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating or updating devices.
 */
public record DeviceRequestDTO(

    @NotBlank(message = "The device name is mandatory and cannot be blank.")
    String name,
    @NotBlank(message = "The device brand is mandatory and cannot be blank.")
    String brand,
    @NotNull(message = "The device state is mandatory.")
    DeviceState state
) {}
