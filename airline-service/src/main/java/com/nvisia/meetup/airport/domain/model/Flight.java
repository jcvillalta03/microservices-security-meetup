package com.nvisia.meetup.airport.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "flight")
public class Flight {

    @Id
    @GeneratedValue
    Long id;

    public Flight(@NotNull String airline, @NotNull String originatingAirport, @NotNull ZonedDateTime departureTime) {
        this.airline = airline;
        this.originatingAirport = originatingAirport;
        this.departureTime = departureTime;
    }

    public Flight(@NotNull String airline, @NotNull String originatingAirport, @NotNull ZonedDateTime departureTime, Long captainId) {
        this.airline = airline;
        this.originatingAirport = originatingAirport;
        this.departureTime = departureTime;
        this.captainId = captainId;
    }

    @NotNull
    String airline;

    @NotNull
    String originatingAirport;

    @NotNull
    ZonedDateTime departureTime;

    Long captainId;

    Long aircraftId;
}
