package com.nvisia.meetup.airport.handler;

import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
public interface FlightRequestHandler {

    ServerResponse getFlights(ServerRequest req);

    ServerResponse getFlight(ServerRequest req);

    ServerResponse getPassengersOnFlight(ServerRequest req);

    ServerResponse addPassengerToFlight(ServerRequest req) throws ServletException, IOException;

    ServerResponse removePassengerFromFlight(ServerRequest req);
}
