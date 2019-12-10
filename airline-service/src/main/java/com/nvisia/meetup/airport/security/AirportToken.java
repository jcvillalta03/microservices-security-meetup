package com.nvisia.meetup.airport.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
public class AirportToken extends AbstractAuthenticationToken {
    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal represented by this authentication
     *                    object.
     */
    public AirportToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    /**
     * The credentials that prove the principal is correct. This is usually a password, but could be anything relevant
     * to the <code>AuthenticationManager</code>. Callers are expected to populate the credentials.
     *
     * @return the credentials that prove the identity of the <code>Principal</code>
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * The identity of the principal being authenticated. In the case of an authentication request with username and
     * password, this would be the username. Callers are expected to populate the principal for an authentication
     * request.
     * <p>
     * The <tt>AuthenticationManager</tt> implementation will often return an
     * <tt>Authentication</tt> containing richer information as the principal for use by
     * the application. Many of the authentication providers will create a {@code UserDetails} object as the principal.
     *
     * @return the <code>Principal</code> being authenticated or the authenticated principal after authentication.
     */
    @Override
    public AirportUserDetails getPrincipal() {
        return null;
    }
}
