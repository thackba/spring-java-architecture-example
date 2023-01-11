package com.example.demo.user.app;

import com.example.demo.user.domain.User;
import com.example.demo.user.ports.in.AddUser;
import com.example.demo.user.ports.out.GenerateUserId;
import com.example.demo.user.ports.out.SaveUser;
import com.example.demo.user.ports.out.UserNameExists;

import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.stereotype.Component;

@Component
public class AddUserUseCase implements AddUser {

	final private MeterRegistry meterRegistry;
	final private GenerateUserId generateUserId;
	final private SaveUser saveUser;
	final private UserNameExists userNameExists;

	public AddUserUseCase(MeterRegistry meterRegistry, GenerateUserId generateUserId, SaveUser saveUser,
			UserNameExists userNameExists) {
		this.meterRegistry = meterRegistry;
		this.generateUserId = generateUserId;
		this.saveUser = saveUser;
		this.userNameExists = userNameExists;
	}

	@Override
	public AddUser.Result execute(AddUser.Command command) {
		return AddUser.Command.validator.applicative().andThen(cmd -> {
			if (userNameExists.userNameExists(cmd.name())) {
				return new AddUser.Result(Status.DUPLICATE_NAME, null);
			}
			String userId = generateUserId.generateUserId();
			User user = new User(userId, command.name());
			saveUser.saveUser(user);
			meterRegistry.counter("user_save").increment();
			return new AddUser.Result(Status.OK, user.id());
		}).validate(command).orElseGet(cvs -> new AddUser.Result(Status.INVALID_COMMAND, null));

	}
}
