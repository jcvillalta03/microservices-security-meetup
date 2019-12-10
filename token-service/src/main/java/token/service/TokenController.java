package token.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.util.DateUtils;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;

import javax.inject.Inject;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Date;

@Requires(beans = {
        KeyPair.class,
        JWK.class
})
@Validated
@Controller("/token")
public class TokenController {

    private final String issuer;
    private KeyPair keyPair;
    private JWK jwk;

    @Inject
    public TokenController(@Value("${jwt.issuer}") String issuer,
                           KeyPair keyPair,
                           JWK jwk) {
        this.issuer = issuer;
        this.keyPair = keyPair;
        this.jwk = jwk;
    }

    @Post(produces = "application/json")
    public TokenResponse getToken(@Body TokenRequest tokenRequest) throws JOSEException {
        // Create RSA-signer with the private key
        JWSSigner signer = new RSASSASigner(keyPair.getPrivate());

        JWTClaimsSet jwtClaimsSet = buildClaimSet(tokenRequest);
        // Prepare JWS object with simple string as payload
        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.RS256)
                        .keyID(jwk.getKeyID())
                        .build(),
                new Payload(jwtClaimsSet.toJSONObject())
        );

        // Compute the RSA signature
        jwsObject.sign(signer);
        String jwt = jwsObject.serialize();
        return new TokenResponse(jwt, tokenRequest.getExpirationTimeSec());
    }

    @Get("/keys.json")
    public String getJwk() {
        return "{\"keys\":[" + jwk.toJSONString() + "]}";
    }

    JWTClaimsSet buildClaimSet(TokenRequest tokenRequest) {
        Date now = new Date();
        return new JWTClaimsSet.Builder()
                .issuer(issuer)
                .issueTime(now)
                .expirationTime(DateUtils.fromSecondsSinceEpoch(
                        DateUtils.toSecondsSinceEpoch(now) + tokenRequest.getExpirationTimeSec()))
                .audience(Collections.emptyList())
                .claim("userId", tokenRequest.getUserId())
                .claim("airline", tokenRequest.getAirline())
                .claim("roles", Collections.singletonList(tokenRequest.getRole()))
                .build();
    }
}