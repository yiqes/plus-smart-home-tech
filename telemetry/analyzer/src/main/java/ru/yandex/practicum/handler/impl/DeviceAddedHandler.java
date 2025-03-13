package ru.yandex.practicum.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.handler.HubHandler;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repository.SensorRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceAddedHandler implements HubHandler {
    private final SensorRepository sensorRepository;

    @Override
    public String getTypeOfPayload() {
        return DeviceAddedEventAvro.class.getName();
    }

    @Override
    @Transactional
    public void handle(HubEventAvro hubEventAvro) {
        DeviceAddedEventAvro addedEventAvro = (DeviceAddedEventAvro) hubEventAvro.getPayload();
        log.info("Device added: {}", addedEventAvro);
        if (!sensorRepository.existsByIdInAndHubId(List.of(addedEventAvro.getId()), hubEventAvro.getHubId())) {
            Sensor newSensor = Sensor.builder()
                    .hubId(hubEventAvro.getHubId())
                    .id(addedEventAvro.getId())
                    .build();
            sensorRepository.save(newSensor);
        }
    }
}