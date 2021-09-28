package com.nashtech.rootkies.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                // config auth
                                .components(new Components()
                                                .addSecuritySchemes("bearer-key-admin",
                                                                new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer").bearerFormat("JWT"))
                                                .addSecuritySchemes("bearer-key-user",
                                                                new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer").bearerFormat("JWT")))
                                // config list of server to test
                                .servers(Arrays.asList(new Server().url("http://localhost:9994/asset-management/"),
                                                new Server().url(
                                                                "https://java-backend-group03-test.azurewebsites.net/asset-management/"),
                                                new Server().url(
                                                                "https://java-web-group03-prod.azurewebsites.net/asset-management/")))
                                // info
                                .info(new Info().title("Asset management team 3 API").description("Sample OpenAPI 3.0")
                                                .contact(new Contact().email("nccuong281299@gmail.com").name("NCC")
                                                                .url(""))
                                                .license(new License().name("Apache 2.0")
                                                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                                                .version("1.0.0"));
        }
}
