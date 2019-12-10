package com.nvisia.meetup.airport.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Data
@NoArgsConstructor
public class AirlineFlightWithPassengers {
    @NotNull
    String airline;

    @NotNull
    Long flightId;

    @NotNull
    List<Passenger> passengerList;

    String airlineName;

    String headquarters;

    public AirlineFlightWithPassengers(@NotNull String airline, @NotNull Long flightId, @NotNull List<Passenger> passengerList) {
        this.airline = airline;
        this.flightId = flightId;
        this.passengerList = passengerList;
    }
}
