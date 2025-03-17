package ru.yandex.practicum.service;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.service.handler.BaseSensorEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Service
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {

    public TemperatureSensorEventHandler(KafkaConfig.KafkaEventProducer producer, KafkaConfig kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEventProto event) {
        TemperatureSensorProto tempEvent = event.getTemperatureSensorEvent();

        return new TemperatureSensorAvro(
                event.getId(),
                event.getHubId(),
                event.getTimestamp().getSeconds(),
                tempEvent.getTemperatureC(),
                tempEvent.getTemperatureF()
        );
    }
}