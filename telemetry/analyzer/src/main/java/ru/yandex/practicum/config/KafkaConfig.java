package ru.yandex.practicum.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaConfig {
    @Value("${analyzer.kafka.server}")
    String kafkaBootstrapServers;
    @Value("${analyzer.kafka.snapshot.group.id}")
    String snapshotGroupId;
    @Value("${analyzer.kafka.snapshot.topic}")
    @Getter
    String inSnapshotTopic;
    @Value("${analyzer.kafka.snapshot.deserializer}")
    String deserializerSnapshotClassName;

    @Value("${analyzer.kafka.hub.group.id}")
    String hubGroupId;
    @Value("${analyzer.kafka.hub.topic}")
    @Getter
    String inHubTopic;
    @Value("${analyzer.kafka.hub.deserializer}")
    String deserializerHubClassName;


    @Bean(name = "SnapshotConsumer")
    public KafkaConsumer<String, SensorsSnapshotAvro> getKafkaSnapshotConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, snapshotGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializerSnapshotClassName);
        return new KafkaConsumer<>(props);
    }

    @Bean(name = "HubConsumer")
    public KafkaConsumer<String, HubEventAvro> getKafkaHubConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, hubGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializerHubClassName);
        return new KafkaConsumer<>(props);
    }

}