package com.maersk.booking.service;

import com.maersk.booking.model.AvailabilityResponse;
import com.maersk.booking.model.BookingRequest;
import com.maersk.booking.model.ContainerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@SpringBootTest
class AvailabilityServiceTest {

    private AvailabilityService availabilityService;
    private WebClient mockWebClient;
    private WebClient.RequestBodyUriSpec uriSpec;
    private WebClient.RequestBodySpec bodySpec;
    private WebClient.RequestHeadersSpec<?> headersSpec;

    @BeforeEach
    void setUp() {
        mockWebClient = mock(WebClient.class);
        uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        bodySpec = mock(WebClient.RequestBodySpec.class);
        headersSpec = mock(WebClient.RequestHeadersSpec.class);

        availabilityService = new AvailabilityService(mockWebClient);
    }

    private BookingRequest createBookingRequest() {
        return BookingRequest.builder()
                .containerType(ContainerType.DRY)
                .containerSize(20)
                .origin("Southampton")
                .destination("Singapore")
                .quantity(5)
                .build();
    }

    @Test
    void testCheckAvailability_Available() {
        BookingRequest request = createBookingRequest();

        when(mockWebClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri("/api/bookings/checkAvailable")).thenReturn(bodySpec);

        // Mock bodyValue() using thenAnswer to return the same bodySpec for chaining
        when(bodySpec.bodyValue(request)).thenAnswer(invocation -> bodySpec);

        // Mock exchangeToMono() to return a Mono of AvailableSpaceResponse
        when(bodySpec.exchangeToMono(ArgumentMatchers.any()))
                .thenReturn(Mono.just(new AvailabilityService.AvailableSpaceResponse(6)));

        Mono<AvailabilityResponse> result = availabilityService.checkAvailability(request);

        StepVerifier.create(result)
                .expectNextMatches(resp -> resp.isAvailable())
                .verifyComplete();
    }

    @Test
    void testCheckAvailability_NotAvailable() {
        BookingRequest request = createBookingRequest();

        when(mockWebClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri("/api/bookings/checkAvailable")).thenReturn(bodySpec);
        when(bodySpec.bodyValue(request)).thenAnswer(invocation -> bodySpec);
        when(bodySpec.exchangeToMono(ArgumentMatchers.any()))
                .thenReturn(Mono.just(new AvailabilityService.AvailableSpaceResponse(0)));

        Mono<AvailabilityResponse> result = availabilityService.checkAvailability(request);

        StepVerifier.create(result)
                .expectNextMatches(resp -> !resp.isAvailable())
                .verifyComplete();
    }

    @Test
    void testCheckAvailability_ErrorFallback() {
        BookingRequest request = createBookingRequest();

        when(mockWebClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri("/api/bookings/checkAvailable")).thenReturn(bodySpec);
        when(bodySpec.bodyValue(request)).thenAnswer(invocation -> bodySpec);
        when(bodySpec.exchangeToMono(ArgumentMatchers.any()))
                .thenReturn(Mono.error(new RuntimeException("Service down")));

        Mono<AvailabilityResponse> result = availabilityService.checkAvailability(request);

        StepVerifier.create(result)
                .expectNextMatches(resp -> !resp.isAvailable())
                .verifyComplete();
    }
}
