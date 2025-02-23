package ru.yandex.practicum.model.hub;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceAddedEvent extends HubEvent {
    String id;
    DeviceTypeAvro type;

    @Override
    public String getType() {
        return "DEVICE_ADDED_EVENT";
    }
}