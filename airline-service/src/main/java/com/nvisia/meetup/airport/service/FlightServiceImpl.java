package com.nvisia.meetup.airport.service;

import com.nvisia.meetup.airport.domain.model.Flight;
import com.nvisia.meetup.airport.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    @Override
    public List<Flight> getFlights() {
        return flightRepository.findAll();
    }

    @Override
    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> flightNotFound(id));
    }

    private ResponseStatusException flightNotFound(Long id) {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Could not find flight with id: [%s]", id)
        );
    }
}
