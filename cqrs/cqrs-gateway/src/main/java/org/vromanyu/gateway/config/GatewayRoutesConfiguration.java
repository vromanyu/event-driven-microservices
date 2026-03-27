package org.vromanyu.gateway.config;

import org.springframework.cloud.gateway.server.mvc.filter.AfterFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.Bucket4jFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.time.Duration;

@Configuration
public class GatewayRoutesConfiguration {

    @Bean
    public RouterFunction<ServerResponse> queryRoutes() {
        return RouterFunctions.route()
                .route(RequestPredicates.path("api.gateway/api/v1/products/get/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.stripPrefix())
                .before(BeforeFilterFunctions.addRequestHeader("X-Gateway", "true"))
                .filter(Bucket4jFilterFunctions.rateLimit(c -> {
                    c.setCapacity(1);
                    c.setPeriod(Duration.ofSeconds(1));
                    c.setKeyResolver(key -> key.remoteAddress()
                            .map(address -> address.getAddress().getHostAddress())
                            .orElse("unknown"));
                }))
                .filter(LoadBalancerFilterFunctions.lb("query-ms"))
                .after(AfterFilterFunctions.addResponseHeader("X-Gateway", "true"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> queryMsOpenApiRoute() {
        return RouterFunctions.route()
                .route(RequestPredicates.path("api.gateway/query-ms/v3/api-docs"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.stripPrefix())
                .filter(LoadBalancerFilterFunctions.lb("query-ms"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> writeMsOpenApiRoute() {
        return RouterFunctions.route()
                .route(RequestPredicates.path("api.gateway/write-ms/v3/api-docs"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.stripPrefix())
                .filter(LoadBalancerFilterFunctions.lb("write-ms"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> writeRoutes() {
        return RouterFunctions.route()
                .route(RequestPredicates.path("api.gateway/api/v1/products/write/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.stripPrefix())
                .before(BeforeFilterFunctions.addRequestHeader("X-Gateway", "true"))
                .filter(Bucket4jFilterFunctions.rateLimit(c -> {
                    c.setCapacity(1);
                    c.setPeriod(Duration.ofSeconds(1));
                    c.setKeyResolver(key -> key.remoteAddress()
                            .map(address -> address.getAddress().getHostAddress())
                            .orElse("unknown"));
                }))
                .filter(LoadBalancerFilterFunctions.lb("write-ms"))
                .after(AfterFilterFunctions.addResponseHeader("X-Gateway", "true"))
                .build();
    }
}
