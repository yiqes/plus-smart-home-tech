package ru.yandex.practicum.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.handler.HubContextHandler;
import ru.yandex.practicum.handler.HubHandler;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubEventProcessor implements Runnable {
    private final HubContextHandler hubContext;
    private final KafkaConsumer<String, HubEventAvro> consumer;
    private final KafkaConfig config;

    @Override
    public void run() {
        try {
            consumer.subscribe(List.of(config.getInHubTopic()));
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(Duration.ofMillis(500));
                if (records.isEmpty()) continue;
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    log.info("topic = {}, partition = {}, offset = {}, record = {}\n",
                            record.topic(), record.partition(), record.offset(), record.value());
                    HubHandler handler = hubContext.getContext().get(record.value().getPayload().getClass().getName());
                    if (handler != null) {
                        handler.handle(record.value());
                        consumer.commitSync();
                    }
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