package ru.yandex.practicum.model.hub;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceRemovedEvent extends HubEvent {
    String id;

    @Override
    public String getType() {
        return "DEVICE_REMOVED_EVENT";
    }
}