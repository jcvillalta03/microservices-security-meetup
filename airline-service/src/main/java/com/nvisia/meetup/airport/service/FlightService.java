package com.nvisia.meetup.airport.service;

import com.nvisia.meetup.airport.domain.model.Flight;

import java.util.List;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
public interface FlightService {

    List<Flight> getFlights();

    Flight getFlightById(Long id);
}
