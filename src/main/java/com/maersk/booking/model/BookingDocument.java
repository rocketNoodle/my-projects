package com.maersk.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bookings")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDocument {
    @Id
    private String bookingRef;
    private Integer containerSize;
    private ContainerType containerType;
    private String origin;
    private String destination;
    private Integer quantity;
    private String timestamp;
}
