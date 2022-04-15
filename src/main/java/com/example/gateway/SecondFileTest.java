package com.example.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

public class SecondFileTest {

    public static void addMoreRoutes(RouteLocatorBuilder.Builder routeBuilder) {
        routeBuilder
                .route(p -> p
                        .path("/facts")
                        .uri("https://cat-fact.herokuapp.com"));
    }
}
