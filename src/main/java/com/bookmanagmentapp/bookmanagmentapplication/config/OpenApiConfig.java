package com.bookmanagmentapp.bookmanagmentapplication.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book Management API")
                        .description("API для управления книгами и авторами")
                        )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Локальный сервер")
                ));
    }
}

