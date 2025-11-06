package com.maersk.booking.repository;

import com.maersk.booking.model.BookingDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BookingRepository extends ReactiveMongoRepository<BookingDocument, String> {
}
