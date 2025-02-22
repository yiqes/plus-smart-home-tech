package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.util.List;

@Service
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;
    private final CommandService commandService;

    public ScenarioService(ScenarioRepository scenarioRepository, CommandService commandService) {
        this.scenarioRepository = scenarioRepository;
        this.commandService = commandService;
    }

    public void processSnapshot(String hubId, SensorsSnapshotAvro snapshot) {
        List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);
        for (Scenario scenario : scenarios) {
            if (checkConditions(scenario, snapshot)) {
                executeActions(scenario, hubId);
            }
        }
    }

    private boolean checkConditions(Scenario scenario, SensorsSnapshotAvro snapshot) {
        for (ScenarioCondition sc : scenario.getConditions()) {
            Condition condition = sc.getCondition();
            String sensorValue = snapshot.getSensorsState().get(condition.getSensorId()).toString();
            if (!evaluateCondition(sensorValue, condition.getOperator(), condition.getValue())) {
                return false;
            }
        }
        return true;
    }

    private boolean evaluateCondition(String sensorValue, String operator, String value) {
        int sensorVal = Integer.parseInt(sensorValue);
        int targetVal = Integer.parseInt(value);
        switch (operator) {
            case "GREATER_THAN": return sensorVal > targetVal;
            case "LOWER_THAN": return sensorVal < targetVal;
            case "EQUALS": return sensorVal == targetVal;
            default: return false;
        }
    }

    private void executeActions(Scenario scenario, String hubId) {
        for (ScenarioAction sa : scenario.getActions()) {
            Action action = sa.getAction();
            commandService.sendAction(hubId, scenario.getName(), action);
        }
    }
}
