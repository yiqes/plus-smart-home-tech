package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AnalyzerApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AnalyzerApplication.class, args);
        final HubEventProcessor eventProcessor = context.getBean(HubEventProcessor.class);
        SnapshotProcessor snapshotProcessor = context.getBean(SnapshotProcessor.class);
        Thread hubThread = new Thread(eventProcessor);
        hubThread.setName("Hub Thread");
        hubThread.start();
        snapshotProcessor.start();
    }
}
