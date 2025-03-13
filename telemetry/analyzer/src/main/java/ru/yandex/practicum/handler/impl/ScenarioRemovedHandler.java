package ru.yandex.practicum.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.handler.HubHandler;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScenarioRemovedHandler implements HubHandler {
    private final ScenarioRepository scenarioRepository;

    @Override
    public String getTypeOfPayload() {
        return ScenarioRemovedEventAvro.class.getName();
    }

    @Override
    @Transactional
    public void handle(HubEventAvro hubEventAvro) {
        ScenarioRemovedEventAvro removedEventAvro = (ScenarioRemovedEventAvro) hubEventAvro.getPayload();
        log.info("Scenario removed: {}", removedEventAvro);
        scenarioRepository.findByHubIdAndName(hubEventAvro.getHubId(),
                removedEventAvro.getName()).ifPresent(scenarioRepository::delete);

    }
}