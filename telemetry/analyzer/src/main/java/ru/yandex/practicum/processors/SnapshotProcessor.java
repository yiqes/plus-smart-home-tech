package ru.yandex.practicum.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.handler.SnapshotHandler;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnapshotProcessor {
    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    private final KafkaConfig config;
    private final SnapshotHandler snapshotHandler;

    public void start() {
        try {
            consumer.subscribe(List.of(config.getInSnapshotTopic()));
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(Duration.ofMillis(500));
                if (records.isEmpty()) continue;
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    log.info("topic = {}, partition = {}, offset = {}, record = {}\n",
                            record.topic(), record.partition(), record.offset(), record.value());
                    snapshotHandler.handle(record.value());
                    consumer.commitSync();
                }
            }
        } catch (WakeupException ignored) {

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            consumer.close();
        }
    }
}