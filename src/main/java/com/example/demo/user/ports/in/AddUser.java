package com.example.demo.user.ports.in;

import com.example.demo.user.domain.User;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.Validator;

public interface AddUser {
	Result execute(Command command);

	record Command(String name) {
		public static Validator<Command> validator = ValidatorBuilder.<Command>of()
				.constraint(Command::name, "name", c -> c.pattern(User.NAME_PATTERN)).build();
	}

	record Result(Status status, String id) {
	}

	enum Status {
		OK, INVALID_COMMAND, DUPLICATE_NAME
	}
}
