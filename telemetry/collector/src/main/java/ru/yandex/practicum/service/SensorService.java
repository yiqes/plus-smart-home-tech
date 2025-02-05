package ru.yandex.practicum.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.sensors.SensorEvent;

@Service
@AllArgsConstructor
public class SensorService {

    private KafkaProducerService kafkaProducerService;

    public void processSensorEvent(SensorEvent event) {
        kafkaProducerService.sendSensorEvent(event);
    }

}