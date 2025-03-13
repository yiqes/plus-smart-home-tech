package ru.yandex.practicum.util;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.praticum.BaseAvroDeserializer;

public class HubDeserializer extends BaseAvroDeserializer<HubEventAvro> {
    public HubDeserializer() {
        super(HubEventAvro.getClassSchema());
    }
}
