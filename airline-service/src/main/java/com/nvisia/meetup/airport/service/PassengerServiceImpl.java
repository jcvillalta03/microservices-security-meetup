package com.nvisia.meetup.airport.service;

import com.nvisia.meetup.airport.domain.model.AirlineFlightWithPassengers;
import com.nvisia.meetup.airport.domain.model.Passenger;
import com.nvisia.meetup.airport.repository.PassengerRepository;
import com.nvisia.meetup.airport.security.AirportAuthorizationChecks;
import com.nvisia.meetup.airport.security.AirportUserDetails;
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
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    private final AirportAuthorizationChecks airportAuthorizationChecks;

    @Override
    public AirlineFlightWithPassengers getPassengersOnFlight(AirportUserDetails userDetails, Long flightId) {
        return airportAuthorizationChecks.onFlightAccess(userDetails, flightId, airline -> {

            //call repository to get passengers by the flight Id
            List<Passenger> flightPassengers = passengerRepository.findAllByFlightId(flightId);
            AirlineFlightWithPassengers airlineFlightWithPassengers =
                    new AirlineFlightWithPassengers(airline, flightId, flightPassengers);

            //TODO: use the airline to grab details about the airline from a different source of data, like an external API.
            // and map the fields back onto the airlineFlightWithPassengersObject

            return airlineFlightWithPassengers;
        });
    }

    @Override
    public Passenger addPassengerToFlight(Long flightId, Passenger passenger) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "This functionality is not implemented yet...");
    }

    @Override
    public void removePassengerFromFlight(Long flightId, Long passengerId) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "This functionality is not implemented yet...");
    }
}
