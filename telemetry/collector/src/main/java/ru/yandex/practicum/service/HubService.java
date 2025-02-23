package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.hub.HubEvent;

@Service
@AllArgsConstructor
public class HubService {

    private KafkaProducerService kafkaProducerService;

    public void processHubEvent(HubEvent event) {
        kafkaProducerService.sendHubEvent(event);
    }

}