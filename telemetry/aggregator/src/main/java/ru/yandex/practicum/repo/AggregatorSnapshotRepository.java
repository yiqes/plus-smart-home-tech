package ru.yandex.practicum.repo;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class AggregatorSnapshotRepository {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro currentSnapshot = snapshots.getOrDefault(event.getHubId(),
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(event.getHubId())
                        .setTimestamp(Instant.now())
                        .setSensorsState(new HashMap<>())
                        .build()
        );
        SensorStateAvro oldSensorState = currentSnapshot.getSensorsState().get(event.getId());
        if (oldSensorState != null
                && oldSensorState.getTimestamp().isBefore(Instant.ofEpochSecond(event.getTimestamp()))
                && oldSensorState.getData().equals(event.getPayload())) {
            return Optional.empty();
        }
        SensorStateAvro newSensorState = new SensorStateAvro(event.getTimestamp(), event.getPayload());
        currentSnapshot.getSensorsState().put(event.getId(), newSensorState);
        currentSnapshot.setTimestamp(Instant.ofEpochSecond(event.getTimestamp()));
        snapshots.put(event.getHubId(), currentSnapshot);
        return Optional.of(currentSnapshot);
    }
}