package org.vromanyu.write.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info =
        @Info(title = "write-ms",
                description = "write-ms responsible for writing/updating",
                version = "1.0")

)
public class OpenApiConfiguration {
}


