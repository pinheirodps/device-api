
package com.example.deviceapi.service;

import com.example.deviceapi.dto.DeviceRequestDTO;
import com.example.deviceapi.dto.DeviceResponseDTO;
import com.example.deviceapi.entity.Device;
import com.example.deviceapi.entity.DeviceState;
import com.example.deviceapi.exception.DeviceInUseException;
import com.example.deviceapi.exception.DeviceNotFoundException;
import com.example.deviceapi.mapper.DeviceMapper;
import com.example.deviceapi.repository.DeviceRepository;
import com.example.deviceapi.service.impl.DeviceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeviceServiceTest {

    private DeviceRepository repository;
    private DeviceMapper mapper;
    private DeviceServiceImpl service;

    @BeforeEach
    void setup() {
        repository = mock(DeviceRepository.class);
        mapper = mock(DeviceMapper.class);
        service = new DeviceServiceImpl(repository, mapper);
    }

    @Test
    void shouldCreateDevice() {
        DeviceRequestDTO request = new DeviceRequestDTO("Device 1", "Brand A", DeviceState.AVAILABLE);
        Device device = new Device();
        device.setCreationTime(LocalDateTime.now());
        DeviceResponseDTO response = new DeviceResponseDTO(1L, "Device 1", "Brand A", DeviceState.AVAILABLE, device.getCreationTime());

        when(mapper.toEntity(request)).thenReturn(device);
        when(repository.save(any(Device.class))).thenReturn(device);
        when(mapper.toDTO(device)).thenReturn(response);

        DeviceResponseDTO result = service.create(request);

        assertEquals("Device 1", result.name());
        verify(repository).save(device);
    }

    @Test
    void shouldUpdateDevice() {
        Long id = 1L;
        Device existing = new Device();
        existing.setState(DeviceState.AVAILABLE);
        DeviceRequestDTO request = new DeviceRequestDTO("Updated", "Brand B", DeviceState.IN_USE);
        DeviceResponseDTO response = new DeviceResponseDTO(id, "Updated", "Brand B", DeviceState.IN_USE, LocalDateTime.now());

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(mapper).update(request, existing);
        when(mapper.toDTO(existing)).thenReturn(response);

        DeviceResponseDTO result = service.update(id, request);

        assertEquals("Updated", result.name());
    }

    @Test
    void shouldNotUpdateInUseDevice() {
        Device device = new Device();
        device.setName("Old");
        device.setName("Brand");
        device.setState(DeviceState.IN_USE);

        when(repository.findById(1L)).thenReturn(Optional.of(device));

        DeviceRequestDTO request = new DeviceRequestDTO("New", "Brand", DeviceState.IN_USE);

        assertThrows(DeviceInUseException.class, () -> service.update(1L, request));
    }

    @Test
    void shouldPartiallyUpdateDevice() {
        Device device = new Device();
        device.setState(DeviceState.AVAILABLE);
        DeviceRequestDTO request = new DeviceRequestDTO("Partial", "Brand", DeviceState.AVAILABLE);
        DeviceResponseDTO response = new DeviceResponseDTO(1L, "Partial", "Brand", DeviceState.AVAILABLE, LocalDateTime.now());

        when(repository.findById(1L)).thenReturn(Optional.of(device));
        doNothing().when(mapper).update(request, device);
        when(mapper.toDTO(device)).thenReturn(response);

        DeviceResponseDTO result = service.partialUpdate(1L, request);

        assertEquals("Partial", result.name());
    }

    @Test
    void shouldNotPartiallyUpdateInUseDeviceChangingNameOrBrand() {
        Device device = new Device();
        device.setState(DeviceState.IN_USE);
        device.setName("Original");
        device.setBrand("BrandX");

        DeviceRequestDTO request = new DeviceRequestDTO("NewName", "BrandX", DeviceState.IN_USE);

        when(repository.findById(1L)).thenReturn(Optional.of(device));

        assertThrows(DeviceInUseException.class, () -> service.partialUpdate(1L, request));
    }

    @Test
    void shouldFindById() {
        Device device = new Device();
        DeviceResponseDTO dto = new DeviceResponseDTO(1L, "Device", "Brand", DeviceState.AVAILABLE, LocalDateTime.now());

        when(repository.findById(1L)).thenReturn(Optional.of(device));
        when(mapper.toDTO(device)).thenReturn(dto);

        DeviceResponseDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals("Device", result.name());
    }

    @Test
    void shouldThrowNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> service.findById(999L));
    }

    @Test
    void shouldFindAll() {
        Device device = new Device();
        DeviceResponseDTO dto = new DeviceResponseDTO(1L, "Device", "Brand", DeviceState.AVAILABLE, LocalDateTime.now());

        when(repository.findAll()).thenReturn(List.of(device));
        when(mapper.toDTO(device)).thenReturn(dto);

        List<DeviceResponseDTO> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void shouldFindByBrand() {
        Device device = new Device();
        DeviceResponseDTO dto = new DeviceResponseDTO(1L, "Device", "Brand", DeviceState.AVAILABLE, LocalDateTime.now());

        when(repository.findByBrand("Brand")).thenReturn(List.of(device));
        when(mapper.toDTO(device)).thenReturn(dto);

        List<DeviceResponseDTO> result = service.findByBrand("Brand");

        assertEquals(1, result.size());
    }


    @Test
    void shouldFindByState() {
        Device device = new Device();
        DeviceResponseDTO dto = new DeviceResponseDTO(1L, "Device", "Brand", DeviceState.AVAILABLE, LocalDateTime.now());

        when(repository.findByState(DeviceState.AVAILABLE)).thenReturn(List.of(device));
        when(mapper.toDTO(device)).thenReturn(dto);

        List<DeviceResponseDTO> result = service.findByState(DeviceState.AVAILABLE);

        assertEquals(1, result.size());
    }


    @Test
    void shouldDeleteDevice() {
        Device device = new Device();
        device.setState(DeviceState.AVAILABLE);

        when(repository.findById(1L)).thenReturn(Optional.of(device));

        service.delete(1L);

        verify(repository).delete(device);
    }

    @Test
    void shouldNotDeleteInUseDevice() {
        Device device = new Device();
        device.setState(DeviceState.IN_USE);

        when(repository.findById(1L)).thenReturn(Optional.of(device));

        assertThrows(DeviceInUseException.class, () -> service.delete(1L));
    }
}

