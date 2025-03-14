package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.model.enums.ActionType;
import ru.yandex.practicum.model.enums.ScenarioConditionOperation;
import ru.yandex.practicum.model.enums.ScenarioConditionType;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;

    public List<Scenario> getScenarioByHubId(String hubId) {
        return scenarioRepository.findByHubId(hubId);
    }

    public void addScenario(ScenarioAddedEventAvro scenarioAddedEvent, String hubId) {
        Scenario scenario = new Scenario();
        scenario.setHubId(hubId);
        scenario.setName(scenarioAddedEvent.getName());

        List<Condition> conditions = scenarioAddedEvent.getConditions().stream()
                .map(conditionEvent -> Condition.builder()
                        .sensorId(conditionEvent.getSensorId())
                        .type(ScenarioConditionType.valueOf(conditionEvent.getType().name()))
                        .operation(ScenarioConditionOperation.valueOf(conditionEvent.getOperation().name()))
                        .value(conditionEvent.getValue())
                        .scenario(scenario)
                        .build())
                .collect(Collectors.toList());

        List<Action> actions = scenarioAddedEvent.getActions().stream()
                .map(actionEvent -> Action.builder()
                        .sensorId(actionEvent.getSensorId())
                        .type(ActionType.valueOf(actionEvent.getType().name()))
                        .value(actionEvent.getValue() != null ? actionEvent.getValue() : 0)
                        .scenario(scenario)
                        .build())
                .collect(Collectors.toList());

        scenario.setActions(actions);
        scenario.setConditions(conditions);

        scenarioRepository.save(scenario);
    }

    public void deleteScenario(String name) {
        scenarioRepository.deleteByName(name);
    }

    private Integer convertToValue(Object value) {
        if (value instanceof Integer) return (Integer) value;
        else if (value instanceof Boolean) return (Boolean) value ? 1 : 0;
        else return null;
    }
}
