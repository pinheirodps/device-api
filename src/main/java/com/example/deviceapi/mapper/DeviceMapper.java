
package com.example.deviceapi.mapper;

import com.example.deviceapi.dto.DeviceRequestDTO;
import com.example.deviceapi.dto.DeviceResponseDTO;
import com.example.deviceapi.entity.Device;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper for converting between Device and DTOs.
 */
@Mapper(componentModel = "spring")
public interface DeviceMapper {
    /**
     * To entity device.
     *
     * @param dto the dto
     * @return the device
     */
    Device toEntity(DeviceRequestDTO dto);

    /**
     * To dto device response dto.
     *
     * @param device the device
     * @return the device response dto
     */
    DeviceResponseDTO toDTO(Device device);

    /**
     * Update.
     *
     * @param dto    the dto
     * @param device the device
     */
    void update(DeviceRequestDTO dto, @MappingTarget Device device);
}
