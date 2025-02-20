package ru.yandex.practicum.grpc.echo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrpcEchoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrpcEchoServerApplication.class, args);
    }

}