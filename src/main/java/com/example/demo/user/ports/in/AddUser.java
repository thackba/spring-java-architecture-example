package com.example.demo.user.ports.in;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ConstraintViolation;
import am.ik.yavi.core.ValueValidator;
import com.example.demo.user.domain.User;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.util.List;

public interface AddUser {
    Try<Result> execute(Command command);

    enum Status {
        OK, DUPLICATE_NAME
    }

    class Command {

        public static ValueValidator<Command, Either<List<ConstraintViolation>, Command>> validator = ValidatorBuilder.<Command>of()
                .constraint(Command::name, "name", c -> c.pattern(User.NAME_PATTERN))
                .build().applicative().andThen(Either::right);
        private final String name;

        private Command(String name) {
            this.name = name;
        }

        public static Either<List<ConstraintViolation>, Command> of(String name) {
            return validator.validate(new Command(name)).orElseGet(Either::left);
        }

        public String name() {
            return name;
        }
    }

    record Result(Status status, String id) {
    }
}
