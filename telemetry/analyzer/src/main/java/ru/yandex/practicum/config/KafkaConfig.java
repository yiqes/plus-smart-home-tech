package ru.yandex.practicum.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Data
@ConfigurationProperties("analyzer.kafka")
@Component
public class KafkaConfig {

    private Properties hubConsumerProperties;
    private Properties snapshotConsumerProperties;
    private Map<String, String> topics;

}