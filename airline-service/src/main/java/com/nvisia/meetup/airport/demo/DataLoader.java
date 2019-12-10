package com.nvisia.meetup.airport.demo;

import com.nvisia.meetup.airport.domain.model.Flight;
import com.nvisia.meetup.airport.domain.model.Passenger;
import com.nvisia.meetup.airport.repository.FlightRepository;
import com.nvisia.meetup.airport.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Component
@RequiredArgsConstructor
@Transactional
public class DataLoader {

    private final FlightRepository flightRepository;

    private final PassengerRepository passengerRepository;

    @PostConstruct
    private void loadTestData() {
        //delete anything in the database first...
        flightRepository.deleteAll();

        List<Flight> flights = new ArrayList<>();

        flights.add(flightRepository.save(new Flight("AA", "ORD", ZonedDateTime.now())));
        flights.add(flightRepository.save(new Flight("UA", "ORD", ZonedDateTime.now())));
        flights.add(flightRepository.save(new Flight("AA", "ORD", ZonedDateTime.now())));
        flights.add(flightRepository.save(new Flight("UA", "ORD", ZonedDateTime.now())));
        flights.add(flightRepository.save(new Flight("AA", "BCN", ZonedDateTime.now())));
        flights.add(flightRepository.save(new Flight("AA", "BCN", ZonedDateTime.now())));
        flights.add(flightRepository.save(new Flight("UA", "SAL", ZonedDateTime.now())));
        flights.add(flightRepository.save(new Flight("AA", "MIA", ZonedDateTime.now())));
        flights.add(flightRepository.save(new Flight("AA", "DEN", ZonedDateTime.now())));
        flights.add(flightRepository.save(new Flight("UA", "DEN", ZonedDateTime.now())));
        flights.add(flightRepository.save(new Flight("AA", "LGA", ZonedDateTime.now())));
        flights.add(flightRepository.save(new Flight("UA", "LGA", ZonedDateTime.now())));
        flights.add(flightRepository.save(new Flight("AA", "LGA", ZonedDateTime.now())));

        flights.forEach(flight -> {
            passengerRepository.save(new Passenger("Cynthia" + flight.getId(), "Passenger", flight));
            passengerRepository.save(new Passenger("Alice" + flight.getId(), "Traveler", flight));
            passengerRepository.save(new Passenger("Bob" + flight.getId(), "Explorer", flight));
        });
    }
}
