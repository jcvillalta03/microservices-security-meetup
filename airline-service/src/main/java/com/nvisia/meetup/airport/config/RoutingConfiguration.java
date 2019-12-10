package com.nvisia.meetup.airport.config;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nvisia.meetup.airport.handler.FlightRequestHandler;
import com.nvisia.meetup.airport.handler.FlightRequestHandlerImpl;
import com.nvisia.meetup.airport.security.AirportAuthorizationChecks;
import com.nvisia.meetup.airport.service.FlightService;
import com.nvisia.meetup.airport.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Configuration
@Profile("!controller")
@RequiredArgsConstructor
public class RoutingConfiguration {

    @Bean
    public FlightRequestHandler flightRequestHandler(final AirportAuthorizationChecks airportAuthorizationChecks,
                                                     final FlightService flightService,
                                                     final PassengerService passengerService,
                                                     final ConfigurableJWTProcessor<SecurityContext> jwtProcessor) {
        return new FlightRequestHandlerImpl(airportAuthorizationChecks, flightService, passengerService, jwtProcessor);
    }

    @Bean
    public RouterFunction<ServerResponse> flightRoutingFunction(FlightRequestHandler flightRequestHandler) {
        return route()
                .nest(RequestPredicates.path(""), builder -> {
                    builder.GET("/flights",
                                request -> flightRequestHandler.getFlights(request));
                    builder.GET("/flights/{id}",
                                request -> flightRequestHandler.getFlight(request));
                    builder.GET("/flights/{id}/passengers",
                                request -> flightRequestHandler.getPassengersOnFlight(request));
                    builder.POST("/flights/{id}/passengers",
                                 request -> flightRequestHandler.addPassengerToFlight(request));
                    builder.DELETE("/flights/{id}/passengers/{passengerId}",
                                   request -> flightRequestHandler.removePassengerFromFlight(request));
                })
                .build();
    }

}
