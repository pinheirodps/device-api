
package com.example.deviceapi.service;

import com.example.deviceapi.dto.DeviceRequestDTO;
import com.example.deviceapi.dto.DeviceResponseDTO;
import com.example.deviceapi.entity.DeviceState;

import java.util.List;

/**
 * Service interface for managing devices.
 *
 * Provides methods for creating, updating, retrieving, and deleting device entities,
 * including filtering by brand or state, with business rules enforced.
 */
public interface DeviceService {

    /**
     * Creates a new device with the given data.
     *
     * @param request the request DTO containing device information
     * @return the created device as a response DTO
     */
    DeviceResponseDTO create(DeviceRequestDTO request);

    /**
     * Fully updates an existing device.
     *
     * @param id the ID of the device to update
     * @param request the updated device data
     * @return the updated device as a response DTO
     */
    DeviceResponseDTO update(Long id, DeviceRequestDTO request);

    /**
     * Partially updates a device.
     *
     * @param id the ID of the device to update
     * @param request the fields to update
     * @return the updated device as a response DTO
     */
    DeviceResponseDTO partialUpdate(Long id, DeviceRequestDTO request);

    /**
     * Retrieves a device by its ID.
     *
     * @param id the device ID
     * @return the device as a response DTO
     */
    DeviceResponseDTO findById(Long id);

    /**
     * Returns all devices in the system.
     *
     * @return a list of device response DTOs
     */
    List<DeviceResponseDTO> findAll();

    /**
     * Finds devices by brand name.
     *
     * @param brand the brand name to filter by
     * @return a list of devices matching the brand
     */
    List<DeviceResponseDTO> findByBrand(String brand);

    /**
     * Finds devices by device state.
     *
     * @param state the device state to filter by
     * @return a list of devices in the given state
     */
    List<DeviceResponseDTO> findByState(DeviceState state);

    /**
     * Deletes a device by its ID.
     *
     * @param id the device ID
     */
    void delete(Long id);
}
