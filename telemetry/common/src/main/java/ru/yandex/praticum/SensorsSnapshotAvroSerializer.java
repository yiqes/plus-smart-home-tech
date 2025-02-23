package ru.yandex.praticum;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public class SensorsSnapshotAvroSerializer extends BaseAvroSerializer<SensorsSnapshotAvro> {
    public SensorsSnapshotAvroSerializer() {
        super(SensorsSnapshotAvro.class);
    }
}
