package ru.yandex.practicum.service;

import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;
import ru.example.smarthome.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.model.Action;

@Service
public class CommandService {
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub stub;

    public CommandService() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();
        this.stub = HubRouterControllerGrpc.newBlockingStub(channel);
    }

    public void sendAction(String hubId, String scenarioName, Action action) {
        DeviceActionProto protoAction = DeviceActionProto.newBuilder()
                .setSensorId(action.getSensorId())
                .setTypeValue(ActionTypeProto.valueOf(action.getType()).getNumber())
                .setValue(action.getValue() != null ? action.getValue() : 0)
                .build();

        DeviceActionRequest request = DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(scenarioName)
                .setAction(protoAction)
                .setTimestamp(Timestamp.newBuilder().setSeconds(System.currentTimeMillis() / 1000))
                .build();

        stub.handleDeviceAction(request);
    }
}