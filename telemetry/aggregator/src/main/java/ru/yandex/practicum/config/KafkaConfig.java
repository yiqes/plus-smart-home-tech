package ru.yandex.practicum.config;

import jakarta.annotation.PreDestroy;
import lombok.*;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.service.GeneralAvroSerializer;
import ru.yandex.practicum.service.SensorEventDeserializer;

import java.time.Duration;
import java.util.*;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties("aggregator.kafka")
public class KafkaConfig {
    //@Value("${aggregator.topic.sensor}")
    String topicTelemetrySensors = "telemetry.sensors.v1";
    //@Value("${aggregator.topic.hub}")
    String topicTelemetrySnapshots = "telemetry.snapshots.v1";
    //@Value("${aggregator.kafka.server}")
    String kafkaBootstrapServers = "localhost:9092";
    public ProducerConfig producer;
    public ConsumerConfig consumer;
    private Map<String, String> topics;

    public enum TopicType {
        TELEMETRY_SENSORS, TELEMETRY_SNAPSHOTS;

        public static TopicType from(String type) {
            switch (type) {
                case "telemetry-sensors" -> {
                    return TopicType.TELEMETRY_SENSORS;
                }
                case "telemetry-snapshots" -> {
                    return TopicType.TELEMETRY_SNAPSHOTS;
                }
                default -> throw new RuntimeException("Topic type not found");

            }
        }
    }

//    @Getter
//    public static class Topics {
//        private final EnumMap<TopicType, String> topics = new EnumMap<>(TopicType.class);
//
//        public Topics(Map<String, String> topics) {
//            for (Map.Entry<String, String> entry : topics.entrySet()) {
//                this.topics.put(TopicType.from(entry.getKey()), entry.getValue());
//            }
//        }
//    }

    @Getter
    public static class ProducerConfig {
        private final Properties properties;
        private final EnumMap<TopicType, String> topics = new EnumMap<>(TopicType.class);


        public ProducerConfig(Properties properties, Map<String, String> topics) {
            this.properties = properties;
            for (Map.Entry<String, String> entry : topics.entrySet()) {
                this.topics.put(TopicType.from(entry.getKey()), entry.getValue());
            }
        }
    }

    @Getter
    public static class ConsumerConfig {
        private final Properties properties;
        private final EnumMap<TopicType, String> topics = new EnumMap<>(TopicType.class);

        public ConsumerConfig(Properties properties, Map<String, String> topics) {
            this.properties = properties;
            for (Map.Entry<String, String> entry : topics.entrySet()) {
                this.topics.put(TopicType.from(entry.getKey()), entry.getValue());
            }
        }
    }
    @Bean
    public KafkaProducer<String, SpecificRecordBase> kafkaProducer() {
        Properties props = new Properties();
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG, 2_000);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG, 10_000);
        return new KafkaProducer<>(props);
    }

    @Bean
    public KafkaConsumer<String, SpecificRecordBase> kafkaConsumer() {
        Properties props = new Properties();
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, "aggregator");
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        return new KafkaConsumer<>(props);
    }

    @Bean
    public EnumMap<TopicType, String> topics() {
        ProducerConfig producer = new ProducerConfig(new Properties(),
                Map.of("telemetry-sensors", topicTelemetrySensors, "telemetry-snapshots", topicTelemetrySnapshots));
        return producer.getTopics();
    }

    @Component
    @RequiredArgsConstructor
    public static class KafkaEventProducer {
        private final KafkaProducer<String, SpecificRecordBase> kafkaProducer;
        private final EnumMap<TopicType, String> topics;

        public <T extends SpecificRecordBase> void send(String topic, String key, T event) {
            ProducerRecord<String, SpecificRecordBase> record =
                    new ProducerRecord<>(topic, key, event);
            kafkaProducer.send(record);
        }

        @PreDestroy
        public void closeProducer() {
            kafkaProducer.flush();
            kafkaProducer.close(Duration.ofSeconds(10));
        }
    }

    @Component
    @RequiredArgsConstructor
    public static class KafkaEventConsumer {
        private final KafkaConsumer<String, SpecificRecordBase> kafkaConsumer;
        private final EnumMap<TopicType, String> topics;

        public <T extends SpecificRecordBase> void consume(String topic) {
            kafkaConsumer.subscribe(List.of(topic));
        }

        public ConsumerRecords<String, SpecificRecordBase> poll() {
            return kafkaConsumer.poll(Duration.ofSeconds(1));
        }

        public void commitAsync() {
            kafkaConsumer.commitAsync();
        }


        @PreDestroy
        public void closeConsumer() {
            kafkaConsumer.close(Duration.ofSeconds(10));
        }
    }
}