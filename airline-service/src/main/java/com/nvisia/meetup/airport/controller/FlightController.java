package com.nvisia.meetup.airport.controller;

import com.nvisia.meetup.airport.domain.model.AirlineFlightWithPassengers;
import com.nvisia.meetup.airport.domain.model.Flight;
import com.nvisia.meetup.airport.domain.model.Passenger;
import com.nvisia.meetup.airport.security.AirportToken;
import com.nvisia.meetup.airport.service.FlightService;
import com.nvisia.meetup.airport.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Profile("controller")
public class FlightController {

    private final FlightService flightService;

    private final PassengerService passengerService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/flights")
    public List<Flight> getFlights() {
        return flightService.getFlights();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/flights/{id}")
    public Flight getFlight(@PathVariable("id") final Long id) {
        return flightService.getFlightById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/flights/{id}/passengers")
    public AirlineFlightWithPassengers getPassengersOnFlight(@AuthenticationPrincipal AirportToken airportToken, @PathVariable("id") final Long id) {
        return passengerService.getPassengersOnFlight(airportToken.getPrincipal(), id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/flights/{id}/passengers")
    public Passenger addPassengerToFlight(@PathVariable("id") final Long id,
                                          @Valid @RequestBody final Passenger passenger) {
        return passengerService.addPassengerToFlight(id, passenger);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/flights/{id}/passengers/{passengerId}")
    public void removePassengerFromFlight(@PathVariable("id") final Long id,
                                          @PathVariable("passengerId") final Long passengerId) {
        passengerService.removePassengerFromFlight(id, passengerId);
    }

}
