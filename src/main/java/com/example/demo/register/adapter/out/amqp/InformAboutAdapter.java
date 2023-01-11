package com.example.demo.register.adapter.out.amqp;

import com.example.demo.events.envelope.v1.EnvelopeV10.Envelope;
import com.example.demo.events.envelope.v1.EnvelopeV10.Envelope.Metadata;
import com.example.demo.events.register.v1.RegisterV10;
import com.example.demo.events.register.v2.RegisterV20;
import com.example.demo.register.domain.Login;
import com.example.demo.register.ports.out.InformAboutLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class InformAboutAdapter implements InformAboutLogin {

	private static final String exchange = "demo.register";
	private final RabbitTemplate rabbitTemplate;
	Logger logger = LoggerFactory.getLogger(InformAboutAdapter.class);

	public InformAboutAdapter(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public void informAboutLogin(Login login) {
		Envelope envelope = null;
		if (login.name().length() % 2 == 0) {
			var newLoginEvent = RegisterV10.NewLoginEvent.newBuilder().setName(login.name()).build();
			var registerEvent = RegisterV10.RegisterEvent.newBuilder().setNewLogin(newLoginEvent).build();
			var metadata = Metadata.newBuilder().setSchema("register").setMajorVersion(1).setMinorVersion(0).build();
			envelope = Envelope.newBuilder().setMetadata(metadata).setPayload(registerEvent.toByteString()).build();
		} else {
			var newLoginEvent = RegisterV20.NewLoginEvent.newBuilder().setName(login.name()).setReason("X").build();
			var registerEvent = RegisterV20.RegisterEvent.newBuilder().setNewLogin(newLoginEvent).build();
			var metadata = Metadata.newBuilder().setSchema("register").setMajorVersion(2).setMinorVersion(0).build();
			envelope = Envelope.newBuilder().setMetadata(metadata).setPayload(registerEvent.toByteString()).build();
		}
		Message message = new Message(envelope.toByteArray());
		rabbitTemplate.convertAndSend(exchange, "", message);
	}
}
