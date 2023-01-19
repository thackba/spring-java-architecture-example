package com.example.demo.user.domain;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ConstraintViolation;
import am.ik.yavi.core.ValueValidator;
import io.vavr.control.Either;

import java.util.List;
import java.util.Objects;

public class User {

    public static final String ID_PATTERN = "^[a-zA-Z0-9\\-]{36}$";
    public static final String NAME_PATTERN = "^[a-zA-Z0-9\\-]{6,}$";
    public static ValueValidator<User, Either<List<ConstraintViolation>, User>> validator = ValidatorBuilder.<User>of()
            .constraint(User::id, "id", c -> c.pattern(User.ID_PATTERN))
            .constraint(User::name, "name", c -> c.pattern(User.NAME_PATTERN))
            .build().applicative().andThen(Either::right);

    private final String id;
    private final String name;

    private User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Either<List<ConstraintViolation>, User> of(String id, String name) {
        return validator.validate(new User(id, name)).orElseGet(Either::left);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
