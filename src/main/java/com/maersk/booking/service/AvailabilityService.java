package com.maersk.booking.service;

import com.maersk.booking.model.AvailabilityResponse;
import com.maersk.booking.model.BookingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class AvailabilityService {

    private final WebClient webClient;

    public AvailabilityService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<AvailabilityResponse> checkAvailability(BookingRequest request) {
        return Mono.defer(() -> webClient.post()
                        .uri("/api/bookings/checkAvailable")
                        .bodyValue(request)
                        .exchangeToMono(response -> {
                            if (response.statusCode().is2xxSuccessful()) {
                                return response.bodyToMono(AvailableSpaceResponse.class);
                            } else {
                                return Mono.error(new RuntimeException("Booking service returned error: " + response.statusCode()));
                            }
                        }))
                .timeout(Duration.ofSeconds(2))
                .retry(3)
                .map(resp -> new AvailabilityResponse(resp != null && resp.availableSpace() > 0))
                .onErrorResume(ex -> {
                    // log.error("Failed to check availability", ex);
                    return Mono.just(new AvailabilityResponse(false));
                });
    }



    public record AvailableSpaceResponse(int availableSpace) {}
}
