package com.example.deviceapi.controller;

import com.example.deviceapi.dto.DeviceRequestDTO;
import com.example.deviceapi.dto.DeviceResponseDTO;
import com.example.deviceapi.entity.DeviceState;
import com.example.deviceapi.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

import java.util.List;

/**
 * REST controller for managing devices.
 *
 * Exposes endpoints to create, retrieve, update, and delete devices.
 * Validates input data and enforces domain rules via the service layer.
 */
@RestController
@RequestMapping("/api/v1/devices")
@AllArgsConstructor
@Tag(name = "Devices", description = "Operations related to device management")
public class DeviceController {

    private DeviceService deviceService;

    /**
     * Create device response dto.
     *
     * @param dto the dto
     * @return the device response dto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new device",
            description = "Creates and returns a new device. The creation time is set automatically.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Device created",
                            content = @Content(schema = @Schema(implementation = DeviceResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error"),
            }
    )
    public DeviceResponseDTO create(@Valid @RequestBody DeviceRequestDTO dto) {
        return deviceService.create(dto);
    }

    /**
     * Update device response dto.
     *
     * @param id  the id
     * @param dto the dto
     * @return the device response dto
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Fully update a device",
            description = "Replaces a device entirely. Devices in IN_USE state cannot be updated.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Device updated",
                            content = @Content(schema = @Schema(implementation = DeviceResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error"),
                    @ApiResponse(responseCode = "404", description = "Device not found"),
                    @ApiResponse(responseCode = "409", description = "Conflict - Device is in use and cannot be updated",
                            content = @Content(schema = @Schema(implementation = Problem.class)))
            }
    )
    public DeviceResponseDTO update(
            @Parameter(description = "Device ID") @PathVariable Long id,
            @Valid @RequestBody DeviceRequestDTO dto
    ) {
        return deviceService.update(id, dto);
    }

    /**
     * Partial update device response dto.
     *
     * @param id  the id
     * @param dto the dto
     * @return the device response dto
     */
    @Operation(
            summary = "Partially update a device",
            description = """
                   Updates specific fields of a device. 
                   If the device is in state `IN_USE`, the `name` and `brand` cannot be changed. 
                   Attempting to change these fields while in `IN_USE` state will result in a 409 Conflict error.
                   Returns 200 and the updated device if successful.""",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Device updated successfully",
                            content = @Content(schema = @Schema(implementation = DeviceResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Device not found",
                            content = @Content),
                    @ApiResponse(responseCode = "409", description = "Conflict - Attempt to change restricted fields while device is IN_USE",
                            content = @Content(schema = @Schema(implementation = Problem.class)))
            }
    )
    @PatchMapping("/{id}")
    public DeviceResponseDTO partialUpdate(
            @Parameter(description = "Device ID") @PathVariable Long id,
            @RequestBody DeviceRequestDTO dto
    ) {
        return deviceService.partialUpdate(id, dto);
    }

    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get a device by ID",
            description = "Retrieves a specific device based on the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Device retrieved successfully",
                            content = @Content(schema = @Schema(implementation = DeviceResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Device not found")
            }
    )
    public DeviceResponseDTO getById(
            @Parameter(description = "Device ID") @PathVariable Long id
    ) {
        return deviceService.findById(id);
    }

    /**
     * Gets all.
     *
     * @return the all
     */
    @GetMapping
    @Operation(
            summary = "List all devices",
            description = "Returns a list of all registered devices",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Devices retrieved successfully",
                            content = @Content(schema = @Schema(implementation = DeviceResponseDTO.class)))
            }
    )
    public List<DeviceResponseDTO> getAll() {
        return deviceService.findAll();
    }

    /**
     * Gets by brand.
     *
     * @param brand the brand
     * @return the by brand
     */
    @GetMapping("/brand/{brand}")
    @Operation(
            summary = "Get devices by brand",
            description = "Retrieves all devices that match the given brand name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Devices retrieved successfully",
                            content = @Content(schema = @Schema(implementation = DeviceResponseDTO.class)))
            }
    )
    public List<DeviceResponseDTO> getByBrand(
            @Parameter(description = "Device brand") @PathVariable String brand
    ) {
        return deviceService.findByBrand(brand);
    }

    /**
     * Gets by state.
     *
     * @param state the state
     * @return the by state
     */
    @GetMapping("/state/{state}")
    @Operation(
            summary = "Get devices by state",
            description = "Retrieves all devices with the specified state",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Devices retrieved successfully",
                            content = @Content(schema = @Schema(implementation = DeviceResponseDTO.class)))
            }
    )
    public List<DeviceResponseDTO> getByState(
            @Parameter(description = "Device state (e.g., ACTIVE, INACTIVE)") @PathVariable DeviceState state
    ) {
        return deviceService.findByState(state);
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    @Operation(
            summary = "Delete a device",
            description = """
        Deletes a device by its ID.

        Devices in state `IN_USE` cannot be deleted and will return a 409 Conflict error.
        Returns 204 No Content if deletion is successful.
        """,
            responses = {
                    @ApiResponse(responseCode = "204", description = "Device deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Device not found", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Conflict - Device in use cannot be deleted",
                            content = @Content(schema = @Schema(implementation = Problem.class)))
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "Device ID") @PathVariable Long id
    ) {
        deviceService.delete(id);
    }
}
