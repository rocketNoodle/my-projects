package com.maersk.booking.controller;

import com.maersk.booking.model.*;
import com.maersk.booking.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private AvailabilityService availabilityService;

    private BookingRequest request;

    @BeforeEach
    void setUp() {
        request = new BookingRequest(
                20,
                ContainerType.DRY,
                "London",
                "Singapore",
                5,
                "2020-10-12T13:53:09Z"
        );
    }

    @Test
    void testCheckAvailability_ReturnsAvailable() {
        Mockito.when(availabilityService.checkAvailability(Mockito.any()))
                .thenReturn(Mono.just(new AvailabilityResponse(true)));

        webTestClient.post()
                .uri("/api/bookings/check-availability")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.available").isEqualTo(true);
    }

    @Test
    void testCreateBooking_ReturnsBookingRef() {
        Mockito.when(bookingService.createBooking(Mockito.any()))
                .thenReturn(Mono.just(new BookingResponse("957000001")));

        webTestClient.post()
                .uri("/api/bookings")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.bookingRef").isEqualTo("957000001");
    }
}

