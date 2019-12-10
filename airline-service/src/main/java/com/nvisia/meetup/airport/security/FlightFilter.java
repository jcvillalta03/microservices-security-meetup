package com.nvisia.meetup.airport.security;

import lombok.Data;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Data
public class FlightFilter {

    Long userId;

    AirportRole role;

    String airline;

    String airport;

}
