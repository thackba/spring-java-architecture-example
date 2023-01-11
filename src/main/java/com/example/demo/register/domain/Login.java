package com.example.demo.register.domain;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.Validator;

import java.util.Objects;

public record Login(String name) {

    public static final String NAME_PATTERN = "^[a-zA-Z\\-0-9]{6,}$";

    public static Validator<Login> validator = ValidatorBuilder.<Login>of()
            .constraint(Login::name, "name", c -> c.pattern(Login.NAME_PATTERN)).build();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Login login = (Login) o;
        return name.equals(login.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
