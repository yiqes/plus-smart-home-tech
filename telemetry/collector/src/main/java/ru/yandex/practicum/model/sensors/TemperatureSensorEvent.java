package ru.yandex.practicum.model.sensors;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TemperatureSensorEvent extends SensorEvent {
    int temperatureC;
    int temperatureF;

    @Override
    public String getType() {
        return "TEMPERATURE_SENSOR_EVENT";
    }
}
