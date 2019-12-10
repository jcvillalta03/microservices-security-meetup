package com.nvisia.meetup.airport.repository;

import com.nvisia.meetup.airport.domain.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    List<Passenger> findAllByFlightId(Long flightId);

}
