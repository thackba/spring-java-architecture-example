package com.example.demo.user.ports.in;

import com.example.demo.user.domain.User;
import io.vavr.control.Option;
import io.vavr.control.Try;

public interface GetUser {
    Try<Option<User>> execute(Query query);

    record Query(String id) {
    }
}
