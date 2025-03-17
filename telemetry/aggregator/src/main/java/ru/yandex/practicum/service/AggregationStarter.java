package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    //private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    protected final KafkaConfig.KafkaEventProducer producer;
    protected final KafkaConfig.KafkaEventConsumer consumer;
    //private final KafkaConsumer<String, SensorEventAvro> consumerR;
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();
    private final EnumMap<KafkaConfig.TopicType, String> topics;

    public void start() {
        final String telemetrySensors = topics.get(KafkaConfig.TopicType.TELEMETRY_SENSORS);
        final String telemetrySnapshots = topics.get(KafkaConfig.TopicType.TELEMETRY_SNAPSHOTS);

        try {
            consumer.consume(telemetrySensors);
            log.info("subscribe -> topic: {}", telemetrySensors);

            while (true) {
                consumer.poll();

                if (consumer.poll().isEmpty()) {
                    continue;
                }

                for (ConsumerRecord<String, SpecificRecordBase> record : consumer.poll().records(telemetrySensors)) {
                    SensorEventAvro event = (SensorEventAvro) record.value();

                    updateState(event).ifPresent(snapshot -> {
                        try {
                            producer.send(telemetrySnapshots, event.getHubId(),  event);
                            log.info("Snapshot hubId {} -> topic {}", snapshot.getHubId(), telemetrySnapshots);
                        } catch (Exception e) {
                            log.error("Ошибка при отправке снапшота в топик", e);
                        }
                    });
                }

                try {
                    consumer.commitAsync();
                } catch (Exception e) {
                    log.error("commitAsync error ", e);
                }
            }
        } catch (Exception e) {
            log.error("sensor event error ", e);
        } finally {
            try {
                if (producer != null) {
                    producer.closeProducer();
                }
            } catch (Exception e) {
                log.error("producer flush error ", e);
            } finally {
                if (producer != null) {
                    try {
                        producer.closeProducer();
                    } catch (Exception e) {
                        log.error("producer close error", e);
                    }
                }

                if (consumer != null) {
                    try {
                        consumer.closeConsumer();
                    } catch (Exception e) {
                        log.error("consumer close error", e);
                    }
                }
            }
        }
    }

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro snapshot = snapshots.getOrDefault(event.getHubId(),
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(event.getHubId())
                        .setTimestamp(Instant.ofEpochSecond(System.currentTimeMillis()))
                        .setSensorsState(new HashMap<>())
                        .build());

        SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());
        if (oldState != null
                && !oldState.getTimestamp().isBefore(Instant.ofEpochSecond(event.getTimestamp()))
                && oldState.getData().equals(event.getPayload())) {
            return Optional.empty();
        }

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp()))
                .setData(event.getPayload())
                .build();

        snapshot.getSensorsState().put(event.getId(), newState);

        snapshot.setTimestamp(Instant.ofEpochSecond(event.getTimestamp()));

        snapshots.put(event.getHubId(), snapshot);

        return Optional.of(snapshot);
    }
}