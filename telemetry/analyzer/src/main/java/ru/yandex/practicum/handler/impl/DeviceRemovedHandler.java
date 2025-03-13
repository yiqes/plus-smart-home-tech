package ru.yandex.practicum.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.handler.HubHandler;
import ru.yandex.practicum.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceRemovedHandler implements HubHandler {
    private final SensorRepository sensorRepository;

    @Override
    public String getTypeOfPayload() {
        return DeviceRemovedEventAvro.class.getName();
    }

    @Override
    @Transactional
    public void handle(HubEventAvro hubEventAvro) {
        DeviceRemovedEventAvro removedEventAvro = (DeviceRemovedEventAvro) hubEventAvro.getPayload();
        log.info("Device removed: {}", removedEventAvro);
        sensorRepository.findByIdAndHubId(removedEventAvro.getId(),
                hubEventAvro.getHubId()).ifPresent(sensorRepository::delete);
    }
}