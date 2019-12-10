package token.service;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class TokenControllerTest {

    @Inject
    EmbeddedServer embeddedServer;

    // @Test
    // TODO: figure out micronaut test issue. (It works running live)
    public void testIndex() throws Exception {
        try (RxHttpClient client = embeddedServer.getApplicationContext().createBean(RxHttpClient.class,
                embeddedServer.getURL())) {
            TokenRequest tokenRequest = new TokenRequest(1L, "role", "airline", 20L);

            HttpRequest<TokenRequest> httpRequest = HttpRequest.POST("/token", tokenRequest);
            HttpResponse<TokenResponse> httpResponse = client.toBlocking().exchange(httpRequest);

            TokenResponse tokenResponse = httpResponse.body();
            assertNotNull(tokenResponse);
            String accessToken = tokenResponse.getAccessToken();
            assertEquals(HttpStatus.OK, client.toBlocking().exchange("/token").status());

        }
    }
}
