package com.example.demo.user.services;

import am.ik.yavi.core.ConstraintViolation;
import com.example.demo.user.domain.User;
import com.example.demo.user.ports.in.AddUser;
import com.example.demo.user.ports.out.GenerateUserId;
import com.example.demo.user.ports.out.SaveUser;
import com.example.demo.user.ports.out.UserNameExists;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddUserService implements AddUser {

    final private MeterRegistry meterRegistry;
    final private GenerateUserId generateUserId;
    final private SaveUser saveUser;
    final private UserNameExists userNameExists;

    public AddUserService(MeterRegistry meterRegistry, GenerateUserId generateUserId, SaveUser saveUser,
                          UserNameExists userNameExists) {
        this.meterRegistry = meterRegistry;
        this.generateUserId = generateUserId;
        this.saveUser = saveUser;
        this.userNameExists = userNameExists;
    }

    @Override
    public Try<Result> execute(Command command) {
        if (userNameExists.userNameExists(command.name())) {
            increaseConflictCounter();
            return Try.of(() -> new Result(Status.DUPLICATE_NAME, null));
        }
        String userId = generateUserId.generateUserId();
        return Try.of(() -> {
            Either<List<ConstraintViolation>, User> user = User.of(userId, command.name());
            if (user.isRight()) {
                saveUser.saveUser(user.get());
                meterRegistry.counter("user_save").increment();
                return new Result(Status.OK, user.get().id());
            } else {
                throw new RuntimeException("This should not happen!");
            }
        });
    }

    private void increaseConflictCounter() {
        meterRegistry.counter("conflict", List.of(
                new ImmutableTag("class", this.getClass().getSimpleName())
        )).increment();
    }
}
