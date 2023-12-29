package com.university.post.config;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.util.*;

@Configuration
@SecurityScheme(
        name = "Xác thực Bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Thiết lập các server dùng để test api
                .servers(List.of(
                        new Server().url("http://localhost:8002"),
                        new Server().url("https://93ea-58-187-228-222.ngrok-free.app")
                ))
                // info
                .info(new Info().title("Magic Post Application API")
                        .description("Document API")
                        .contact(new Contact()
                                .email("phong2642002@gmail.com")
                                .name("Nguyễn Văn Phong")
                                .url("https://nxp.me/"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                        .version("1.0.0"));
    }
}
