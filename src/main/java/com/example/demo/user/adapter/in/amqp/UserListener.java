package com.example.demo.user.adapter.in.amqp;

import com.example.demo.events.envelope.v1.EnvelopeV10.Envelope;
import com.example.demo.events.register.v1.RegisterV10;
import com.example.demo.events.register.v2.RegisterV20;
import com.example.demo.user.ports.in.AddUser;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserListener {

    final private MeterRegistry meterRegistry;
    final private AddUser addUser;

    public UserListener(MeterRegistry meterRegistry, AddUser addUser) {
        this.meterRegistry = meterRegistry;
        this.addUser = addUser;
    }

    // TODO: Use better Listener -> retry and DLQ
    @RabbitListener(queues = "demo.register.user")
    public void receiveUserMessages(Message message) {
        try {
            var envelope = Envelope.parseFrom(message.getBody());
            var metadata = envelope.getMetadata();
            if ("register".equals(metadata.getSchema())) {
                if (1 == metadata.getMajorVersion()) {
                    handleV1Version(envelope.getPayload());
                }
                if (2 == metadata.getMajorVersion()) {
                    handleV1Version(envelope.getPayload());
                    handleV2Version(envelope.getPayload());
                }
            }
        } catch (InvalidProtocolBufferException e) {
            this.meterRegistry
                    .counter("proto_error", List.of(new ImmutableTag("class", this.getClass().getSimpleName())))
                    .increment();
        }
    }

    private void handleV1Version(ByteString data) throws InvalidProtocolBufferException {
        var event = RegisterV10.RegisterEvent.parseFrom(data);
        if (event.hasNewLogin()) {
            var newLogin = event.getNewLogin();
            var command = AddUser.Command.of("V1" + newLogin.getName());
            if (command.isRight()) {
                execute(command.get());
            }
        }
    }

    private void handleV2Version(ByteString data) throws InvalidProtocolBufferException {
        var event = RegisterV20.RegisterEvent.parseFrom(data);
        if (event.hasNewLogin()) {
            var newLogin = event.getNewLogin();
            var command = AddUser.Command.of("V2" + newLogin.getName());
            if (command.isRight()) {
                execute(command.get());
            }
        }
    }

    private void execute(AddUser.Command command) {
        addUser.execute(command);
    }
}
