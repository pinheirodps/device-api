
package com.example.deviceapi.service;

import com.example.deviceapi.dto.DeviceRequestDTO;
import com.example.deviceapi.dto.DeviceResponseDTO;
import com.example.deviceapi.entity.DeviceState;

import java.util.List;

/**
 * Service interface for managing devices.
 */
public interface DeviceService {
    DeviceResponseDTO create(DeviceRequestDTO request);
    DeviceResponseDTO update(Long id, DeviceRequestDTO request);
    DeviceResponseDTO partialUpdate(Long id, DeviceRequestDTO request);
    DeviceResponseDTO findById(Long id);
    List<DeviceResponseDTO> findAll();
    List<DeviceResponseDTO> findByBrand(String brand);
    List<DeviceResponseDTO> findByState(DeviceState state);
    void delete(Long id);
}
