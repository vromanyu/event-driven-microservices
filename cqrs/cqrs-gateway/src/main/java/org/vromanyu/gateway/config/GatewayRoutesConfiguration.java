package org.vromanyu.gateway.config;

import org.springframework.cloud.gateway.server.mvc.filter.AfterFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GatewayRoutesConfiguration {

    @Bean
    public RouterFunction<ServerResponse> queryRoutes() {
        return RouterFunctions.route()
                .route(RequestPredicates.path("api.gateway/api/v1/products/get/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.stripPrefix())
                .before(BeforeFilterFunctions.addRequestHeader("X-Gateway", "true"))
                .filter(LoadBalancerFilterFunctions.lb("query-ms"))
                .after(AfterFilterFunctions.addResponseHeader("X-Gateway", "true"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> writeRoutes() {
        return RouterFunctions.route()
                .route(RequestPredicates.path("api.gateway/api/v1/products/write/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.stripPrefix())
                .before(BeforeFilterFunctions.addRequestHeader("X-Gateway", "true"))
                .filter(LoadBalancerFilterFunctions.lb("write-ms"))
                .after(AfterFilterFunctions.addResponseHeader("X-Gateway", "true"))
                .build();
    }
}
