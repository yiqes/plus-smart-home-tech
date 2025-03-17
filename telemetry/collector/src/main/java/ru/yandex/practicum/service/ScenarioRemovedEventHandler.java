package ru.yandex.practicum.service;


import org.springframework.stereotype.Service;

import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.service.handler.BaseHubEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Service
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {

    public ScenarioRemovedEventHandler(KafkaConfig.KafkaEventProducer producer, KafkaConfig kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEventProto event) {
        ScenarioRemovedEventProto scenarioRemovedEvent = event.getScenarioRemoved();

        return new ScenarioRemovedEventAvro(scenarioRemovedEvent.getName());

    }
}