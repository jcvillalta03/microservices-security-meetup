package com.nvisia.meetup.airport.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTClaimsSetVerifier;
import com.nimbusds.jwt.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Slf4j
@Configuration
public class JwtVerificationConfiguration {
    private static final KeyFactory KEY_FACTORY;
    private static final String PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----";
    private static final String PUBLIC_KEY_FOOTER = "-----END PUBLIC KEY-----";
    private static final String NEW_LINE = "\n";
    private static final String LINE_FEED = "\r";

    static {
        try {
            KEY_FACTORY = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Bean
    public JWSVerificationKeySelector<SecurityContext> jwsVerificationKeySelector(@Value("${jwt.jwkUrl}") String jwkUrl) throws
                                                                                                                         MalformedURLException {
        RemoteJWKSet<SecurityContext> keySource = new RemoteJWKSet<>(new URL(jwkUrl));
        return new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keySource);
    }

    @Bean
    public JWTClaimsSetVerifier<SecurityContext> claimsSetVerifier(@Value("${jwt.issuer}") String issuer) {
        return (claimsSet, context) -> {
            Date now = new Date();
            Date expirationTime = claimsSet.getExpirationTime();

            if (!DateUtils.isAfter(expirationTime, now, 0L)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                                  String.format("The JWT is expired (exp: [%s])", expirationTime));
            }

            if (null == issuer || !issuer.equals(claimsSet.getIssuer())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(
                        "The token issuer does not match the known issuers (tokenIssuer: " + "[%s], knownIssuer: [%s])",
                        claimsSet.getIssuer(), issuer));
            }
        };
    }

    @Bean
    public ConfigurableJWTProcessor<SecurityContext> jwtProcessor(JWTClaimsSetVerifier<SecurityContext> claimsSetVerifier,
                                                                  JWSVerificationKeySelector<SecurityContext> jwsVerificationKeySelector) {
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(jwsVerificationKeySelector);
        jwtProcessor.setJWTClaimsSetVerifier(claimsSetVerifier);
        return jwtProcessor;
    }

    private RSAKey getRsaPublicKey(String publicKeyString) {
        if (StringUtils.isEmpty(publicKeyString)) {
            log.trace("Public Key String not provided");
            return null;
        }

        String strippedKey = publicKeyString;
        strippedKey = strippedKey.replace(PUBLIC_KEY_HEADER, "");
        strippedKey = strippedKey.replace(PUBLIC_KEY_FOOTER, "");
        strippedKey = strippedKey.replace(NEW_LINE, "");
        strippedKey = strippedKey.replace(LINE_FEED, "");

        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(strippedKey));

        RSAPublicKey rsaPublicKey;
        try {
            rsaPublicKey = (RSAPublicKey) KEY_FACTORY.generatePublic(keySpecX509);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Cannot create RSAPublicKey from raw public key", e);
        }
        // Convert to JWK format
        return new RSAKey.Builder(rsaPublicKey).build();
    }

}
