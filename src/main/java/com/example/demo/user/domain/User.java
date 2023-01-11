package com.example.demo.user.domain;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.Validator;

import java.util.Objects;

public record User(String id, String name) {

    public static final String ID_PATTERN = "^[a-zA-Z0-9\\-]{36}$";
    public static final String NAME_PATTERN = "^[a-zA-Z\\-0-9]{6,}$";

    public static Validator<User> validator = ValidatorBuilder.<User>of()
            .constraint(User::id, "id", c -> c.pattern(User.ID_PATTERN))
            .constraint(User::name, "name", c -> c.pattern(User.NAME_PATTERN)).build();

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
