package ru.yandex.practicum.model.sensors;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LightSensorEvent extends SensorEvent {
    int linkQuality;
    int luminosity;

    @Override
    public String getType() {
        return "LIGHT_SENSOR_EVENT";
    }
}