package com.example.demo.user.ports.out;

import com.example.demo.user.domain.User;

import java.util.Optional;

public interface LoadUser {
    Optional<User> loadUser(String id);
}
