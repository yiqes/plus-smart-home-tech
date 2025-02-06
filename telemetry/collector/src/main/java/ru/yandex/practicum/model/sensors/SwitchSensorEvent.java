package ru.yandex.practicum.model.sensors;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SwitchSensorEvent extends SensorEvent {
    boolean state;

    @Override
    public String getType() {
        return "SWITCH_SENSOR_EVENT";
    }
}