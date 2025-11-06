package com.maersk.booking.service;

import com.maersk.booking.model.*;
import com.maersk.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository repository;
    private static final AtomicLong COUNTER = new AtomicLong(957000000);

    public Mono<BookingResponse> createBooking(BookingRequest request) {
        String ref = String.valueOf(COUNTER.incrementAndGet());
        BookingDocument doc = BookingDocument.builder()
                .bookingRef(ref)
                .containerSize(request.getContainerSize())
                .containerType(request.getContainerType())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .quantity(request.getQuantity())
                .timestamp(request.getTimestamp())
                .build();

        return repository.save(doc)
                .map(saved -> new BookingResponse(saved.getBookingRef()))
                .onErrorResume(e -> {
                    // log error
                    return Mono.error(new RuntimeException("Sorry there was a problem processing your request"));
                });
    }
}
