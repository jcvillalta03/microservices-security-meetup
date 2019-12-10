package com.nvisia.meetup.airport.security;

import lombok.Data;

import java.util.Collection;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Data
public class AirportUserDetails {

    Long userId;

    String airline;

    Collection<String> roles;

}
