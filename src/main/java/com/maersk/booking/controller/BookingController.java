package com.maersk.booking.controller;

import com.maersk.booking.model.*;
import com.maersk.booking.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final AvailabilityService availabilityService;
    private final BookingService bookingService;

    @PostMapping("/check-availability")
    public Mono<AvailabilityResponse> checkAvailability(@Valid @RequestBody BookingRequest request) {
        return availabilityService.checkAvailability(request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        return bookingService.createBooking(request);
    }
}
