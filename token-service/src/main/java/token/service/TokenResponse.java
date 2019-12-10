package token.service;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
public class TokenResponse {
    String accessToken;
    Long ttl;

    public TokenResponse(String accessToken, Long ttl) {
        this.accessToken = accessToken;
        this.ttl = ttl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }
}
