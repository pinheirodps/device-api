
package com.example.deviceapi.dto;

import com.example.deviceapi.entity.DeviceState;

import java.time.LocalDateTime;

/**
 * DTO for returning device data to clients.
 */
public record DeviceResponseDTO(
    Long id,
    String name,
    String brand,
    DeviceState state,
    LocalDateTime creationTime
) {}
