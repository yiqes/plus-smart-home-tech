package ru.yandex.practicum.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.handler.HubHandler;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.model.enums.ActionType;
import ru.yandex.practicum.model.enums.ScenarioConditionOperation;
import ru.yandex.practicum.model.enums.ScenarioConditionType;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.repository.SensorRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScenarioAddedHandler implements HubHandler {
    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;

    @Override
    public String getTypeOfPayload() {
        return ScenarioAddedEventAvro.class.getName();
    }

    @Override
    @Transactional
    public void handle(HubEventAvro hubEventAvro) {
        ScenarioAddedEventAvro scenarioAddedEvent = (ScenarioAddedEventAvro) hubEventAvro.getPayload();
        log.info("Scenario added: {}", scenarioAddedEvent);
        if (scenarioRepository.findByHubIdAndName(hubEventAvro.getHubId(), scenarioAddedEvent.getName()).isEmpty()) {
            Scenario scenario = Scenario.builder()
                    .hubId(hubEventAvro.getHubId())
                    .name(scenarioAddedEvent.getName())
                    .conditions(getConditions(scenarioAddedEvent.getConditions()))
                    .actions(getActions(scenarioAddedEvent.getActions()))
                    .build();
            scenario.getActions().forEach(a -> a.setScenario(scenario));
            scenarioRepository.save(scenario);
        }
    }

    private List<Action> getActions(List<DeviceActionAvro> actions) {

        return actions.stream()
                .map(a -> Action.builder()
                        .sensor(sensorRepository.findById((long) a.getSensorId()).orElseThrow())
                        .type(ActionType.valueOf(a.getType().name()))
                        .value(a.getValue() == null ? 0 : a.getValue())
                        .build())
                .toList();
    }

    private List<Condition> getConditions(List<ScenarioConditionAvro> typeAvros) {
        return typeAvros.stream()
                .map(c -> Condition.builder()
                        .type(ScenarioConditionType.valueOf(c.getType().name()))
                        .sensor(sensorRepository.findById(Long.valueOf(c.getSensorId())).orElseThrow())
                        .operation(ScenarioConditionOperation.valueOf(c.getOperation().name()))
                        .value(convertToValue(c.getValue()))
                        .build())
                .toList();
    }

    private Integer convertToValue(Object value) {
        if (value instanceof Integer) return (Integer) value;
        else if (value instanceof Boolean) return (Boolean) value ? 1 : 0;
        else return null;
    }
}