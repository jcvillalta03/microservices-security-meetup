package com.nvisia.meetup.airport.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Slf4j
@RequiredArgsConstructor
public class AirportAuthorizationChecksImpl implements AirportAuthorizationChecks {

    private final EntityManager entityManager;

    @Override
    public boolean canAccessFlight(final AirportUserDetails userDetails,
                                   final Long targetId,
                                   final String permissionString) {
        return onFlightAccess(userDetails,
                              targetId,
                              Objects::nonNull);
    }

    /**
     * Given a flight id determine the airline of the user matched that of the flight if the subject has access to the
     * airline then pass the airline an input to the supplied "action" function
     * <p>
     * ## Access Rules: - admin users can access any airline's flight data - airline service reps can access their own
     * airlines flights - flight crew can access their own flights
     * <p>
     * ## Example
     * <pre>
     *  public Flight getFlight(Long id) {
     *
     *      AirportUserDetails userDetails = ...;
     *
     *      return authorizationChecks.onFlightAccess(
     *          userDetails,
     *          id,
     *          permission,
     *          airline -> flightRepository.getFlightBy( airline, flightId)
     *      );
     * }
     * </pre>
     *
     * @param userDetails - the object containing the extracted results of the subject's jwt. {@link
     *                    AirportUserDetails}
     * @param flightId    - the id of the flight whose airline will be retrieved to determine if the subject has access
     *                    to it.
     * @param action      - the action to execute on success. This action will accept the airline as input
     * @return the output of the action
     */
    @Override
    public <T> T onFlightAccess(final AirportUserDetails userDetails,
                                final Long flightId,
                                final Function<String, T> action) {
        return getFlightAirline(flightId)
                .map(airline -> checkAccessAndGetAirline(userDetails, airline))
                .map(permittedAirline -> action.apply(permittedAirline))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                                                               "Access is Denied"));
    }

    private Optional<String> getFlightAirline(Long entityId) {
        TypedQuery<String> query = entityManager
                .createQuery("SELECT airline from Flight where id = :id", String.class)
                .setParameter("id", entityId);
        try {
            final String airport = query.getSingleResult();
            return Optional.of(airport);
        } catch (NoResultException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                              String.format("Flight with id: [%s] not found", entityId));
        }
    }

    private String checkAccessAndGetAirline(AirportUserDetails userDetails,
                                            String airline) {
        if (userDetails.getRoles().contains("admin") ||
                userDetails.getRoles().contains("flight_crew") && userDetails.getAirline().equals(airline)) {
            return airline;
        }
        log.error("Non-admin user from airline: {} tried to access resource from airline {}", userDetails.getAirline(),
                  airline);
        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                          "Access is Denied");
    }

}
