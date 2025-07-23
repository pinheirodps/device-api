
package com.example.deviceapi.repository;

import com.example.deviceapi.entity.Device;
import com.example.deviceapi.entity.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for Device entity.
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {
    /**
     * Find by brand list.
     *
     * @param brand the brand
     * @return the list
     */
    List<Device> findByBrand(String brand);

    /**
     * Find by state list.
     *
     * @param state the state
     * @return the list
     */
    List<Device> findByState(DeviceState state);
}
