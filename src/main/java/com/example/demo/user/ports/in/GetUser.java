package com.example.demo.user.ports.in;

import com.example.demo.user.domain.User;

import java.util.Optional;

public interface GetUser {
    Optional<User> execute(String id);
}
