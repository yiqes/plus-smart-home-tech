package ru.yandex.practicum.util;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.praticum.BaseAvroDeserializer;

public class HubEventDeserializer extends BaseAvroDeserializer<HubEventAvro> {
    public HubEventDeserializer() {
        super(HubEventAvro.getClassSchema());
    }
}
