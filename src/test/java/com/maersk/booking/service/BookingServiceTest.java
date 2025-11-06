package com.maersk.booking.service;

import com.maersk.booking.controller.BookingController;
import com.maersk.booking.model.BookingRequest;
import com.maersk.booking.model.BookingResponse;
import com.maersk.booking.model.ContainerType;
import com.maersk.booking.service.BookingService;
import com.maersk.booking.service.AvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class BookingControllerUnitTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private AvailabilityService availabilityService;

    @InjectMocks
    private BookingController bookingController;

    private BookingRequest request;

    @BeforeEach
    void setUp() {
        request = new BookingRequest(20, ContainerType.DRY, "London", "Singapore", 5, "2020-10-12T13:53:09Z");
    }

    @Test
    void testCheckAvailability_ReturnsAvailable() {
        Mockito.when(availabilityService.checkAvailability(Mockito.any()))
                .thenReturn(Mono.just(new com.maersk.booking.model.AvailabilityResponse(true)));

        StepVerifier.create(bookingController.checkAvailability(request))
                .expectNextMatches(resp -> resp.isAvailable())
                .verifyComplete();
    }

    @Test
    void testCreateBooking_ReturnsBookingRef() {
        Mockito.when(bookingService.createBooking(Mockito.any()))
                .thenReturn(Mono.just(new BookingResponse("957000001")));

        StepVerifier.create(bookingController.createBooking(request))
                .expectNextMatches(resp -> resp.getBookingRef().equals("957000001"))
                .verifyComplete();
    }
}



