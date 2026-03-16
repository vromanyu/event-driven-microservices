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
    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .route(RequestPredicates.path("/api/orders/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.addRequestHeader("X-Gateway", "true"))
                .filter(LoadBalancerFilterFunctions.lb("order-ms"))
                .after(AfterFilterFunctions.addResponseHeader("X-Gateway", "true"))
                .build();
    }
}
