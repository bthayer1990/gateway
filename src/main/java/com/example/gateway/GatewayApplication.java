package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
@RestController
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
        String httpUri = uriConfiguration.getHttpbin();

        var routeBuilder = builder.routes();

        routeBuilder
                .route(p -> p
                        .path("/get")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri(httpUri))
                .route(p -> p
                        .host("*.circuitbreaker.com")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("mycmd")
                                        .setFallbackUri("forward:/fallback")))
                        .uri(httpUri));

        SecondFileTest.addMoreRoutes(routeBuilder);

        return routeBuilder.build();
    }

    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("fallback");
    }
}