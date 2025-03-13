package ru.yandex.practicum.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.model.enums.ScenarioConditionOperation;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.service.HubClientGrpc;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SnapshotHandler {
    private final ScenarioRepository scenarioRepository;
    private final HubClientGrpc hubClient;

    public void handle(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        log.info("Processing snapshot for hubId: {}", hubId);
        scenarioRepository.findByHubId(hubId).stream()
                .filter(s -> isStart(s, snapshot))
                .forEach(s -> executeActions(s.getActions(), hubId));
    }

    private boolean isStart(Scenario scenario, SensorsSnapshotAvro snapshot) {
        for (Condition condition : scenario.getConditions()) {
            if (!checkCondition(condition, snapshot)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkCondition(Condition condition, SensorsSnapshotAvro snapshot) {
        SensorStateAvro sensorState = snapshot.getSensorsState().get(condition.getSensor().getId());

        if (sensorState == null) {
            log.warn("Sensor data for sensorId {} is missing in the snapshot", condition.getSensor().getId());
            return false;
        }
        return switch (condition.getType()) {
            case TEMPERATURE -> evaluateCondition(((TemperatureSensorAvro) sensorState.getData()).getTemperatureC(),
                    condition.getOperation(), condition.getValue());
            case HUMIDITY -> evaluateCondition(((ClimateSensorAvro) sensorState.getData()).getHumidity(),
                    condition.getOperation(), condition.getValue());
            case CO2LEVEL -> evaluateCondition(((ClimateSensorAvro) sensorState.getData()).getCo2Level(),
                    condition.getOperation(), condition.getValue());
            case LUMINOSITY -> evaluateCondition(((LightSensorAvro) sensorState.getData()).getLuminosity(),
                    condition.getOperation(), condition.getValue());
            case MOTION -> evaluateCondition(((MotionSensorAvro) sensorState.getData()).getMotion() ? 1 : 0,
                    condition.getOperation(), condition.getValue());
            case SWITCH -> evaluateCondition(((SwitchSensorAvro) sensorState.getData()).getState() ? 1 : 0,
                    condition.getOperation(), condition.getValue());
            default -> {
                log.warn("Unsupported condition type: {}", condition.getType());
                yield false;
            }
        };
    }

    private boolean evaluateCondition(int sensorValue, ScenarioConditionOperation operation, int targetValue) {
        return switch (operation) {
            case EQUALS -> sensorValue == targetValue;
            case GREATER_THAN -> sensorValue > targetValue;
            case LOWER_THAN -> sensorValue < targetValue;
        };
    }

    private void executeActions(List<Action> actions, String hubId) {
        actions.forEach(action -> hubClient.sendAction(action, hubId));
    }
}