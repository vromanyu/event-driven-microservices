package org.vromanyu.gateway;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableWireMock(@ConfigureWireMock(port = 8085))
public class GatewayTests {

    private final RestClient restClient = RestClient.create("http://localhost:8585");

    @Test
    public void givenValidOrderId_shouldReturnOk() {
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/orders/1")).willReturn(WireMock.aResponse().withStatus(200)));
        ResponseEntity<Void> response = restClient.get().uri("/api/orders/1").retrieve().toBodilessEntity();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getHeaders().containsHeader("X-Gateway")).isTrue();
    }

    @Test
    public void givenInvalidOrderId_shouldReturnNotFound() {
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/orders/1")).willReturn(WireMock.badRequest()));

        Assertions.assertThatThrownBy(() -> {
            ResponseEntity<Void> response = restClient.get().uri("/api/orders/1").retrieve().toBodilessEntity();
            Assertions.assertThat(response).isNotNull();
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            Assertions.assertThat(response.getHeaders().containsHeader("X-Gateway")).isTrue();
        });
    }

    @Test
    public void givenOrderRequest_shouldReturnCreated() {
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/api/orders")).willReturn(WireMock.aResponse().withStatus(201)));
        ResponseEntity<Void> response = restClient.post().uri("/api/orders").retrieve().toBodilessEntity();
    }

}
