package com.nvisia.meetup.airport.config;

import com.nvisia.meetup.airport.security.AirportAuthorizationChecks;
import com.nvisia.meetup.airport.security.AirportAuthorizationChecksImpl;
import com.nvisia.meetup.airport.security.AirportPermissionEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;

import javax.persistence.EntityManager;

/**
 * Declare beans from our custom authorization classes
 *
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Configuration
public class SecurityConfiguration {

    /**
     * Create an airport authorization checks singleton bean
     *
     * @return
     */
    @Bean
    public AirportAuthorizationChecks authorizationChecks(EntityManager entityManager) {
        return new AirportAuthorizationChecksImpl(entityManager);
    }

    /**
     * Create a bean from our custom {@link PermissionEvaluator} class, using the authorization checks class
     *
     * @param airportAuthorizationChecks
     * @return
     */
    @Bean
    public PermissionEvaluator permissionEvaluator(AirportAuthorizationChecks airportAuthorizationChecks) {
        return new AirportPermissionEvaluator(airportAuthorizationChecks);
    }

}
