package ru.yandex.practicum.handler;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubHandler {
    String getTypeOfPayload();

    void handle(HubEventAvro hubEventAvro);
}