
package com.example.deviceapi.controller;

import com.example.deviceapi.dto.DeviceRequestDTO;
import com.example.deviceapi.dto.DeviceResponseDTO;
import com.example.deviceapi.entity.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class DeviceControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("device_db")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @LocalServerPort
    int port;

    private WebClient client;
    @BeforeEach
    void setUp() {
        client = WebClient.create("http://localhost:" + port);
    }

    @Test
    void testCreateDevice() {
        DeviceRequestDTO dto = new DeviceRequestDTO("Tablet", "BrandZ", DeviceState.AVAILABLE);
        DeviceRequestDTO response = client.post()
                .uri("/api/v1/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(DeviceRequestDTO.class)
                .block();

        assertNotNull(response);
        assertEquals("Tablet", response.name());
    }

    private DeviceResponseDTO createTestDevice(String name, String brand, DeviceState state) {
        DeviceRequestDTO dto = new DeviceRequestDTO(name, brand, state);
        return client.post()
                .uri("/api/v1/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(DeviceResponseDTO.class)
                .block();
    }


    @Test
    void testGetDeviceById() {
        var created = createTestDevice("Phone", "BrandX", DeviceState.AVAILABLE);

        DeviceResponseDTO result = client.get()
                .uri("/api/v1/devices/" + created.id())
                .retrieve()
                .bodyToMono(DeviceResponseDTO.class)
                .block();

        assertEquals(created.id(), result.id());
    }

    @Test
    void testGetAllDevices() {
        createTestDevice("Device1", "Brand1", DeviceState.AVAILABLE);
        createTestDevice("Device2", "Brand2", DeviceState.INACTIVE);

        List<DeviceResponseDTO> result = client.get()
                .uri("/api/v1/devices")
                .retrieve()
                .bodyToFlux(DeviceResponseDTO.class)
                .collectList()
                .block();

        assertNotNull(result);
        assertTrue(result.size() >= 2);
    }

    @Test
    void testGetByBrand() {
        createTestDevice("BrandDevice", "TestBrand", DeviceState.AVAILABLE);

        List<DeviceResponseDTO> result = client.get()
                .uri("/api/v1/devices/brand/TestBrand")
                .retrieve()
                .bodyToFlux(DeviceResponseDTO.class)
                .collectList()
                .block();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("TestBrand", result.get(0).brand());
    }

    @Test
    void testGetByState() {
        createTestDevice("StateDevice", "XYZ", DeviceState.IN_USE);

        List<DeviceResponseDTO> result = client.get()
                .uri("/api/v1/devices/state/IN_USE")
                .retrieve()
                .bodyToFlux(DeviceResponseDTO.class)
                .collectList()
                .block();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(DeviceState.IN_USE, result.get(0).state());
    }

    @Test
    void testUpdateDevice() {
        var created = createTestDevice("Old", "BrandA", DeviceState.AVAILABLE);

        DeviceRequestDTO update = new DeviceRequestDTO("NewName", "BrandB", DeviceState.INACTIVE);
        DeviceResponseDTO updated = client.put()
                .uri("/api/v1/devices/" + created.id())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(update)
                .retrieve()
                .bodyToMono(DeviceResponseDTO.class)
                .block();

        assertNotNull(updated);
        assertEquals("NewName", updated.name());
        assertEquals("BrandB", updated.brand());
        assertEquals(DeviceState.INACTIVE, updated.state());
    }

    @Test
    void testPartialUpdateDevice() {
        var created = createTestDevice("Partial", "BrandP", DeviceState.AVAILABLE);

        DeviceRequestDTO update = new DeviceRequestDTO("PartialUpdated", "BrandP", DeviceState.AVAILABLE);
        DeviceResponseDTO updated = client.patch()
                .uri("/api/v1/devices/" + created.id())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(update)
                .retrieve()
                .bodyToMono(DeviceResponseDTO.class)
                .block();

        assertNotNull(updated);
        assertEquals("PartialUpdated", updated.name());
    }

    @Test
    void testDeleteDevice() {
        var created = createTestDevice("ToDelete", "Trash", DeviceState.AVAILABLE);

        client.delete()
                .uri("/api/v1/devices/" + created.id())
                .retrieve()
                .toBodilessEntity()
                .block();

        var notFound = client.get()
                .uri("/api/v1/devices/" + created.id())
                .exchangeToMono(res -> {
                    assertEquals(404, res.statusCode().value());
                    return res.bodyToMono(String.class);
                })
                .block();

        assertNotNull(notFound);
    }

    @Test
    void testDeleteInUseDeviceShouldFail() {
        var inUse = createTestDevice("InUse", "Locked", DeviceState.IN_USE);

        client.delete()
                .uri("/api/v1/devices/" + inUse.id())
                .exchangeToMono(response -> {
                    assertEquals(409, response.statusCode().value());
                    return response.bodyToMono(String.class);
                })
                .block();
    }

    @Test
    void testUpdateInUseDeviceShouldFail() {
        var inUse = createTestDevice("Busy", "Locked", DeviceState.IN_USE);
        DeviceRequestDTO update = new DeviceRequestDTO("Changed", "Changed", DeviceState.IN_USE);

        client.put()
                .uri("/api/v1/devices/" + inUse.id())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(update)
                .exchangeToMono(response -> {
                    assertEquals(409, response.statusCode().value());
                    return response.bodyToMono(String.class);
                })
                .block();
    }

    @Test
    void testPartialUpdateInUseWithNameChangeShouldFail() {
        var inUse = createTestDevice("Busy", "Locked", DeviceState.IN_USE);
        DeviceRequestDTO update = new DeviceRequestDTO("New", "Locked", DeviceState.IN_USE);

        client.patch()
                .uri("/api/v1/devices/" + inUse.id())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(update)
                .exchangeToMono(response -> {
                    assertEquals(409, response.statusCode().value());
                    return response.bodyToMono(String.class);
                })
                .block();
    }
}
