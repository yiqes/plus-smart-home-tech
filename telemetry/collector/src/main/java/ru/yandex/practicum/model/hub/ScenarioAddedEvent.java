package ru.yandex.practicum.model.hub;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioAddedEvent extends HubEvent {
    String name;
    List<ScenarioConditionAvro> conditions;
    List<DeviceActionAvro> actions;

    @Override
    public String getType() {
        return "SCENARIO_ADDED_EVENT";
    }
}