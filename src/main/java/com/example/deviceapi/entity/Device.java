
package com.example.deviceapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Represents a device entity stored in the database.
 */
@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceState state;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationTime;
}
