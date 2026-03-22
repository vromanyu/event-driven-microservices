package org.vromanyu.gateway;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Body;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;
import tools.jackson.databind.json.JsonMapper;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableWireMock({@ConfigureWireMock(name = "query-ms", port = 8085), @ConfigureWireMock(name = "write-ms", port = 8086)})
public class GatewayTests {

    private final RestClient restClient = RestClient.builder().baseUrl("http://localhost:8585").build();

    @Autowired
    private JsonMapper jsonMapper;

    @InjectWireMock("query-ms")
    private WireMockServer queryMsWireMock;

    @InjectWireMock("write-ms")
    private WireMockServer writeMsWireMock;

    private RestClient getRestClientWithoutRetries() {
        CloseableHttpClient client = HttpClientBuilder.create().disableAutomaticRetries().build();
        return RestClient.builder()
                .requestFactory(new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory(client)))
                .baseUrl("http://localhost:8585")
                .build();
    }

    @Test
    public void whenAllProductsAndThereAreProducts_thenReturnAllProducts() {
        Map<String, Object> products = Map.of("products", List.of(Map.of("id", 1, "name", "cookies")));
        queryMsWireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1/products/get/all"))
                .willReturn(WireMock.jsonResponse(jsonMapper.writeValueAsString(products), HttpStatus.OK.value())));

        ResponseEntity<Map<String, Object>> response = restClient.get().uri("/api.gateway/api/v1/products/get/all").retrieve().toEntity(new ParameterizedTypeReference<>() {
        });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().get("products")).isEqualTo(List.of(Map.of("id", 1, "name", "cookies")));
    }

    @Test
    public void whenAllProductsAndThereAreNoProducts_thenReturnEmptyList() {
        queryMsWireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1/products/get/all"))
                .willReturn(WireMock.jsonResponse(jsonMapper.writeValueAsString(Collections.emptyList()), HttpStatus.OK.value())));

        ResponseEntity<List<Object>> response = restClient.get().uri("/api.gateway/api/v1/products/get/all").retrieve().toEntity(new ParameterizedTypeReference<>() {
        });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).isEmpty();
    }

    @Test
    public void givenValidProductId_shouldReturnOk() {
        Map<String, Object> product = Map.of("id", 1, "name", "cookies");
        queryMsWireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1/products/get/1")).willReturn(WireMock.jsonResponse(jsonMapper.writeValueAsString(product), HttpStatus.OK.value())));
        ResponseEntity<Map<String, Object>> response = restClient.get().uri("/api.gateway/api/v1/products/get/1").retrieve().toEntity(new ParameterizedTypeReference<>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo(product);
    }

    @Test
    public void givenInvalidProductId_shouldReturnNotFound() {
        Map<String, Object> product = Map.of("path", "/api/products/get/1", "error", "product with id 1 not found", "timestamp", OffsetDateTime.now(), "status", HttpStatus.BAD_REQUEST.getReasonPhrase());
        queryMsWireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1/products/get/1")).willReturn(WireMock.badRequest().withResponseBody(Body.fromJsonBytes(jsonMapper.writeValueAsBytes(product)))));

        Assertions.assertThatThrownBy(() ->
        {
            ResponseEntity<Map<String, Object>> response = restClient.get().uri("/api.gateway/api/v1/products/get/1").retrieve().toEntity(new ParameterizedTypeReference<>() {
            });
            Assertions.assertThat(response).isNotNull();
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            Assertions.assertThat(response.getBody()).isNotNull();
            Assertions.assertThat(response.getBody()).isEqualTo(product);
        });
    }

    @Test
    public void givenProductRequest_shouldReturnCreated() {
        Map<String, Object> product = Map.of("id", 1, "name", "cookies", "price", 100, "quantity", 10);
        Map<String, Object> productRequest = Map.of("name", "cookies", "price", 100, "quantity", 10);
        writeMsWireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/api/v1/products/write/"))
                .withHeader("Content-Type", WireMock.containing(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(WireMock.equalToJson(jsonMapper.writeValueAsString(productRequest), true, true))
                .willReturn(WireMock.aResponse().withStatus(201)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withHeader("Location", "/api/prodcuts/get/1").
                        withBody(jsonMapper.writeValueAsBytes(product))));
        ResponseEntity<Map<String, Object>> response = restClient.post().uri("/api.gateway/api/v1/products/write/").body(productRequest).retrieve().toEntity(new ParameterizedTypeReference<>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getHeaders().getLocation()).isNotNull();
        Assertions.assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/api/prodcuts/get/1");
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo(product);
    }

    @Test
    public void givenProductUpdateRequest_shouldReturnUpdatedProduct() {
        Map<String, Object> product = Map.of("id", 1, "name", "cookies", "price", 200, "quantity", 10);
        Map<String, Object> productRequest = Map.of("name", "cookies", "price", 200, "quantity", 10);
        writeMsWireMock.stubFor(WireMock.put(WireMock.urlEqualTo("/api/v1/products/write/1"))
                .withHeader("Content-Type", WireMock.containing(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(WireMock.equalToJson(jsonMapper.writeValueAsString(productRequest), true, true))
                .willReturn(WireMock.aResponse().withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonMapper.writeValueAsBytes(product))));
        ResponseEntity<Map<String, Object>> response = restClient.put().uri("api.gateway/api/v1/products/write/1").body(productRequest).retrieve().toEntity(new ParameterizedTypeReference<>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo(product);
    }

    @Test
    public void givenProductDeleteRequest_shouldReturnNoContent() {
        writeMsWireMock.stubFor(WireMock.delete(WireMock.urlEqualTo("/api/v1/products/write/1")).willReturn(WireMock.noContent()));
        ResponseEntity<Void> response = restClient.delete().uri("/api.gateway/api/v1/products/write/1").retrieve().toBodilessEntity();
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void whenMoreThanOneRequestPerSecond_returnTooManyRequests() {
        Map<String, Object> products = Map.of("products", List.of(Map.of("id", 1, "name", "cookies")));
        queryMsWireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1/products/get/all"))
                .willReturn(WireMock.jsonResponse(jsonMapper.writeValueAsString(products), HttpStatus.OK.value())));
        ResponseEntity<Void> response = restClient.get().uri("/api.gateway/api/v1/products/get/all").retrieve().toBodilessEntity();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThatThrownBy(() -> {
            ResponseEntity<Void> failedRequest = getRestClientWithoutRetries().get().uri("/api.gateway/api/v1/products/get/all")
                    .retrieve()
                    .toBodilessEntity();
            Assertions.assertThat(failedRequest.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        });
    }

}
