
package com.example.deviceapi.service.impl;

import com.example.deviceapi.dto.DeviceRequestDTO;
import com.example.deviceapi.dto.DeviceResponseDTO;
import com.example.deviceapi.entity.Device;
import com.example.deviceapi.entity.DeviceState;
import com.example.deviceapi.exception.DeviceInUseException;
import com.example.deviceapi.exception.DeviceNotFoundException;
import com.example.deviceapi.mapper.DeviceMapper;
import com.example.deviceapi.repository.DeviceRepository;
import com.example.deviceapi.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of device business rules.
 */
@Service
@AllArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private  DeviceRepository repository;
    private  DeviceMapper mapper;

    @Override
    public DeviceResponseDTO create(DeviceRequestDTO request) {
        Device device = mapper.toEntity(request);
        device.setCreationTime(LocalDateTime.now());
        return mapper.toDTO(repository.save(device));
    }

    @Override
    @Transactional
    public DeviceResponseDTO update(Long id, DeviceRequestDTO request) {
        Device device = validateDeviceForUpdate(id, request);
        mapper.update(request, device);
        return mapper.toDTO(device);
    }

    @Override
    @Transactional
    public DeviceResponseDTO partialUpdate(Long id, DeviceRequestDTO request) {
        Device device = validateDeviceForUpdate(id, request);
        mapper.update(request, device);
        return mapper.toDTO(device);
    }


    @Override
    public DeviceResponseDTO findById(Long id) {
        return mapper.toDTO(getDeviceOrThrow(id));
    }

    @Override
    public List<DeviceResponseDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<DeviceResponseDTO> findByBrand(String brand) {
        return repository.findByBrand(brand).stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<DeviceResponseDTO> findByState(DeviceState state) {
        return repository.findByState(state).stream().map(mapper::toDTO).toList();
    }

    @Override
    public void delete(Long id) {
        Device device = getDeviceOrThrow(id);
        if (device.getState() == DeviceState.IN_USE) {
            throw new DeviceInUseException("In-use devices cannot be deleted.");
        }
        repository.delete(device);
    }

    private Device getDeviceOrThrow(Long id) {
        return repository.findById(id).orElseThrow(DeviceNotFoundException::new);
    }
    private Device validateDeviceForUpdate(Long id, DeviceRequestDTO request) {
        Device device = getDeviceOrThrow(id);
        if (device.getState() == DeviceState.IN_USE &&
                (!device.getName().equals(request.name()) || !device.getBrand().equals(request.brand()))) {
            throw new DeviceInUseException();
        }
        return device;
    }
}
