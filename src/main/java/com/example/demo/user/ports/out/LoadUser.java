package com.example.demo.user.ports.out;

import com.example.demo.user.domain.User;
import io.vavr.control.Option;

public interface LoadUser {
    Option<User> loadUser(String id);
}
