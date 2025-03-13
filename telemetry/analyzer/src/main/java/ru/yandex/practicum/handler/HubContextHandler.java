package ru.yandex.practicum.handler;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Getter
public class HubContextHandler {
    private final Map<String, HubHandler> context;

    public HubContextHandler(Set<HubHandler> handlers) {
        context = handlers.stream()
                .collect(Collectors.toMap(HubHandler::getTypeOfPayload, h -> h));
    }
}