package ru.yandex.practicum.service;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.service.handler.BaseSensorEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Service
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> {

    public ClimateSensorEventHandler(KafkaConfig.KafkaEventProducer producer, KafkaConfig kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEventProto event) {
        ClimateSensorProto climateEvent = event.getClimateSensorEvent();
        return new ClimateSensorAvro(
                climateEvent.getTemperatureC(),
                climateEvent.getHumidity(),
                climateEvent.getCo2Level()
        );
    }
}