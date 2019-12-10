package com.nvisia.meetup.airport.handler;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nvisia.meetup.airport.domain.model.AirlineFlightWithPassengers;
import com.nvisia.meetup.airport.domain.model.Flight;
import com.nvisia.meetup.airport.domain.model.Passenger;
import com.nvisia.meetup.airport.security.AirportAuthorizationChecks;
import com.nvisia.meetup.airport.security.AirportUserDetails;
import com.nvisia.meetup.airport.service.FlightService;
import com.nvisia.meetup.airport.service.PassengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.function.ServerResponse.noContent;
import static org.springframework.web.servlet.function.ServerResponse.ok;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Slf4j
@RequiredArgsConstructor
public class FlightRequestHandlerImpl implements FlightRequestHandler {

    public static final String ACCESS_TOKEN_VALUE_PREFIX = "Bearer ";

    private final AirportAuthorizationChecks airportAuthorizationChecks;

    private final FlightService flightService;

    private final PassengerService passengerService;

    private final ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;

    @Override
    public ServerResponse getFlights(final ServerRequest req) {
        //verify that the request is authenticated
        AirportUserDetails principal = verifyAndGetToken(req);

        //parse request parameters, body, variable...

        // call the service layer
        List<Flight> flights = flightService.getFlights();

        //return a successful response
        return ok().body(flights);
    }

    @Override
    public ServerResponse getFlight(final ServerRequest req) {
        //verify that the request is authenticated
        AirportUserDetails principal = verifyAndGetToken(req);

        //parse request parameters, body, variable...
        var id = Long.valueOf(req.pathVariable("id"));

        // call the service layer
        Flight flight = airportAuthorizationChecks.onFlightAccess(principal, id, airline ->
                flightService.getFlightById(id));

        //return a successful response
        return ok().body(flight);
    }

    @Override
    public ServerResponse getPassengersOnFlight(final ServerRequest req) {
        //verify that the request is authenticated
        AirportUserDetails principal = verifyAndGetToken(req);

        //parse request parameters, body, variable...
        var id = Long.valueOf(req.pathVariable("id"));

        // call the service layer
        AirlineFlightWithPassengers passengers = passengerService.getPassengersOnFlight(principal, id);

        //return a successful response
        return ok().body(passengers);
    }

    @Override
    public ServerResponse addPassengerToFlight(final ServerRequest req) throws ServletException, IOException {
        //verify that the request is authenticated
        AirportUserDetails principal = verifyAndGetToken(req);

        //parse request parameters, body, variable...
        var id = Long.valueOf(req.pathVariable("id"));
        var passenger = req.body(Passenger.class);

        // call the service layer
        Passenger createdPassenger = airportAuthorizationChecks.onFlightAccess(principal, id, airline ->
                passengerService.addPassengerToFlight(id, passenger));

        //return a successful response
        return ok().body(createdPassenger);
    }

    @Override
    public ServerResponse removePassengerFromFlight(final ServerRequest req) {
        //verify that the request is authenticated
        AirportUserDetails principal = verifyAndGetToken(req);

        //parse request parameters, body, variable...
        var id = Long.valueOf(req.pathVariable("id"));
        var passengerId = Long.valueOf(req.pathVariable("passengerId"));

        // call the service layer
        passengerService.removePassengerFromFlight(id, passengerId);

        //return a successful response
        return noContent().build();
    }

    /**
     * Extract the JWT from the request headers and then parse it into a user details object
     *
     * @param request
     * @return
     */
    public AirportUserDetails verifyAndGetToken(final ServerRequest request) {
        return Optional
                .of(request)
                .map(ServerRequest::headers)
                .map(headers -> headers.header(HttpHeaders.AUTHORIZATION))
                .filter(headerValues -> !CollectionUtils.isEmpty(headerValues))
                .map(headerValues -> headerValues.get(0))
                .map(headerValue -> parseJwtIntoUserDetails(headerValue, configurableJWTProcessor))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                                               "Error parsing JWT from Authorization header. Cannot " +
                                                                       "proceed without proper user authentication"));
    }

    private AirportUserDetails parseJwtIntoUserDetails(final String authorizationHeaderValue,
                                                       final ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor) {
        try {
            String jwtString = authorizationHeaderValue.replace(ACCESS_TOKEN_VALUE_PREFIX, "");
            JWTClaimsSet claimsSet = getJwtClaimsSet(jwtString);

            AirportUserDetails airportUserDetails = new AirportUserDetails();
            airportUserDetails.setUserId(claimsSet.getLongClaim("userId"));
            airportUserDetails.setRoles(List.of(claimsSet.getStringArrayClaim("roles")));
            airportUserDetails.setAirline(claimsSet.getStringClaim("airline"));
            return airportUserDetails;
        } catch (ParseException | BadJOSEException | JOSEException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error parsing JWT into valid claims set. " +
                    "Cannot proceed without proper user authentication. Error: " + e.getMessage(), e);
        }
    }

    private JWTClaimsSet getJwtClaimsSet(String jwtString) throws ParseException, JOSEException, BadJOSEException {
        return JWTParser.parse(jwtString).getJWTClaimsSet();
    }

//    private JWTClaimsSet getJwtClaimsSet(String jwtString) throws ParseException, JOSEException, BadJOSEException {
//        return configurableJWTProcessor.process(jwtString, null);
//    }
}
