package com.nvisia.meetup.airport.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Slf4j
@RequiredArgsConstructor
public class AirportPermissionEvaluator implements PermissionEvaluator {

    private static final String FLIGHT_TARGET_TYPE = "flight";


    private final AirportAuthorizationChecks airportAuthorizationChecks;

    /**
     * @param authentication     represents the user in question. Should not be null.
     * @param targetDomainObject the domain object for which permissions should be checked. May be null in which case
     *                           implementations should return false, as the null condition can be checked explicitly in
     *                           the expression.
     * @param permission         a representation of the permission object as supplied by the expression system. Not
     *                           null.
     * @return true if the permission is granted, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object targetDomainObject,
                                 Object permission) {
        log.warn("Denying user " + authentication.getName() + " permission '"
                         + permission + "' on object " + targetDomainObject);
        return false;
    }

    /**
     * Alternative method for evaluating a permission where only the identifier of the target object is available,
     * rather than the target instance itself.
     *
     * @param authentication represents the user in question. Should not be null.
     * @param targetId       the identifier for the object instance (usually a Long)
     * @param targetType     a String representing the target's type (usually a Java classname). Not null.
     * @param permission     a representation of the permission object as supplied by the expression system. Not null.
     * @return true if the permission is granted, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        if (!(authentication instanceof AirportToken)) {
            log.error("User is not properly authenticated");
            return false;
        }

        // currently, only support Long types
        if (!(targetId instanceof Long)) {
            log.error("Target ID [{}] not of type Long", targetId);
            return false;
        }

        Long targetUUID = Long.valueOf(String.valueOf(targetId));
        String permissionString = String.valueOf(permission);

        AirportUserDetails airportToken = ((AirportToken) authentication).getPrincipal();
        switch (targetType) {
            case FLIGHT_TARGET_TYPE:
                return airportAuthorizationChecks.canAccessFlight(airportToken, targetUUID, permissionString);
            default:
                log.warn("Cannot evaluate permissions for unknown type: {}", targetType);
                return false;
        }
    }
}
