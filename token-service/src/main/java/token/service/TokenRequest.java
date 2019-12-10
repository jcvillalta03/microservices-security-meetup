package token.service;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotNull;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Introspected
public class TokenRequest {

    @NotNull
    private Long userId;

    @NotNull
    private String role;

    @NotNull
    private String airline;

    @NotNull
    private Long expirationTimeSec;

    public TokenRequest(@NotNull Long userId, @NotNull String role, @NotNull String airline, @NotNull Long expirationTimeSec) {
        this.userId = userId;
        this.role = role;
        this.airline = airline;
        this.expirationTimeSec = expirationTimeSec;
    }

    public TokenRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public Long getExpirationTimeSec() {
        return expirationTimeSec;
    }

    public void setExpirationTimeSec(Long expirationTimeSec) {
        this.expirationTimeSec = expirationTimeSec;
    }
}
