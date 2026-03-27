package org.vromanyu.query.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info =
        @Info(title = "query-ms",
                description = "query-ms responsible for querying",
                version = "1.0"),
        servers = {
                @Server(url = "/api.gateway")
        }

)
public class OpenApiConfiguration {

}
