package ru.yandex.practicum.processors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.repository.ScenarioRepository;

@Component
public class HubEventProcessor {
    private final ScenarioRepository scenarioRepository;

    public HubEventProcessor(ScenarioRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @KafkaListener(topics = "telemetry.hubs.v1", groupId = "analyzer-group")
    public void processHubEvent(HubEventAvro event) {

    }
}