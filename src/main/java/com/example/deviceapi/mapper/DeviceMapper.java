
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
    Device toEntity(DeviceRequestDTO dto);
    DeviceResponseDTO toDTO(Device device);
    void update(DeviceRequestDTO dto, @MappingTarget Device device);
}
