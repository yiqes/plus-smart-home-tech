package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.service.handler.BaseSensorEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;


@Service
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {

    public SwitchSensorEventHandler(KafkaConfig.KafkaEventProducer producer, KafkaConfig kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEventProto event) {
        SwitchSensorProto switchEvent = event.getSwitchSensorEvent();

        return new SwitchSensorAvro(switchEvent.getState());
    }
}