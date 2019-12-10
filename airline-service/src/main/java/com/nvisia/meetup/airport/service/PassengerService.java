package com.nvisia.meetup.airport.service;

import com.nvisia.meetup.airport.domain.model.AirlineFlightWithPassengers;
import com.nvisia.meetup.airport.domain.model.Passenger;
import com.nvisia.meetup.airport.security.AirportUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
public interface PassengerService {

    AirlineFlightWithPassengers getPassengersOnFlight(AirportUserDetails userDetails, Long flightId);

    @PreAuthorize("hasPermission(#flightId, 'flight', 'add-passenger')")
    Passenger addPassengerToFlight(Long flightId, Passenger passenger);

    @PreAuthorize("hasPermission(#flightId, 'flight', 'remove-passenger')")
    void removePassengerFromFlight(Long flightId, Long passengerId);
}
