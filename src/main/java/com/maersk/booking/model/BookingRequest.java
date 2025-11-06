package com.maersk.booking.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {
    @NotNull
    @Min(20)
    @Max(40)
    private Integer containerSize;

    @NotNull
    private ContainerType containerType;

    @NotBlank
    @Size(min = 5, max = 20)
    private String origin;

    @NotBlank
    @Size(min = 5, max = 20)
    private String destination;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer quantity;

    private String timestamp;
}

