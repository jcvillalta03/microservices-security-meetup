package com.nvisia.meetup.airport.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "passenger")
public class Passenger {

    @Id
    @GeneratedValue
    Long id;

    @NotNull
    String firstName;

    @NotNull
    String lastName;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "flight_id")
    Flight flight;

    public Passenger(@NotNull String firstName, @NotNull String lastName, @NotNull Flight flight) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.flight = flight;
    }
}
