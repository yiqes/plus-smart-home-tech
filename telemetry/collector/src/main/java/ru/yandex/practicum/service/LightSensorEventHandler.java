package ru.yandex.practicum.service;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.service.handler.BaseSensorEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Service
public class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {

    public LightSensorEventHandler(KafkaConfig.KafkaEventProducer producer, KafkaConfig kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    protected LightSensorAvro mapToAvro(SensorEventProto event) {
        LightSensorProto lightEvent = event.getLightSensorEvent();
        return new LightSensorAvro(
                lightEvent.getLinkQuality(),
                lightEvent.getLuminosity()
        );
    }
}