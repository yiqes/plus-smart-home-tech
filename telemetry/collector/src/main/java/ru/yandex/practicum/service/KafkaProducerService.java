package ru.yandex.practicum.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.sensors.SensorEvent;
import ru.yandex.practicum.model.hub.HubEvent;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, SensorEvent> sensorKafkaTemplate;
    private final KafkaTemplate<String, HubEvent> hubKafkaTemplate;
    private static final String SENSORS_TOPIC = "telemetry.sensors.v1";
    private static final String HUBS_TOPIC = "telemetry.hubs.v1";

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, SensorEvent> sensorKafkaTemplate,
                                KafkaTemplate<String, HubEvent> hubKafkaTemplate) {
        this.sensorKafkaTemplate = sensorKafkaTemplate;
        this.hubKafkaTemplate = hubKafkaTemplate;
    }

    public void sendSensorEvent(SensorEvent event) {
        sensorKafkaTemplate.send(SENSORS_TOPIC, event.getId(), event);
    }

    public void sendHubEvent(HubEvent event) {
        hubKafkaTemplate.send(HUBS_TOPIC, event.getHubId(), event);
    }
}