package com.nvisia.meetup.airport.repository;

import com.nvisia.meetup.airport.domain.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

}
