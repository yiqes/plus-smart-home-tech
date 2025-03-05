package ru.yandex.practicum.processors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.ScenarioService;

@Component
public class SnapshotProcessor {
    private final ScenarioService scenarioService;

    public SnapshotProcessor(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @KafkaListener(topics = "telemetry.snapshots.v1", groupId = "analyzer-group")
    public void processSnapshot(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        scenarioService.processSnapshot(hubId, snapshot);
    }
}