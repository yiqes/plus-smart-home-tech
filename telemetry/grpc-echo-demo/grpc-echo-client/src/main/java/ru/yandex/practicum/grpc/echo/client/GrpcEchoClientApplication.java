package ru.yandex.practicum.grpc.echo.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GrpcEchoClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrpcEchoClientApplication.class, args);
    }

}