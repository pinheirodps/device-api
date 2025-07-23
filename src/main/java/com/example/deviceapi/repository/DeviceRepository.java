
package com.example.deviceapi.repository;

import com.example.deviceapi.entity.Device;
import com.example.deviceapi.entity.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for Device entity.
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByBrand(String brand);
    List<Device> findByState(DeviceState state);
}
