package com.nvisia.meetup.airport.security;

import java.util.function.Function;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
public interface AirportAuthorizationChecks {

    /**
     * Given user's details (from the JWT), determine if user has the specified permission to access the flight
     *
     * @param userDetails
     * @param flightId
     * @param permissionString
     * @return
     */
    boolean canAccessFlight(final AirportUserDetails userDetails,
                            final Long flightId,
                            final String permissionString);

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
    <T> T onFlightAccess(AirportUserDetails userDetails,
                         Long flightId,
                         Function<String, T> action);
}
