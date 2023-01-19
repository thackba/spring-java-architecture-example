package com.example.demo.user.services;

import com.example.demo.user.domain.User;
import com.example.demo.user.ports.in.GetUser;
import com.example.demo.user.ports.out.LoadUser;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.springframework.stereotype.Component;

@Component
public class GetUserService implements GetUser {

    final private LoadUser loadUser;

    public GetUserService(LoadUser loadUser) {
        this.loadUser = loadUser;
    }

    @Override
    public Try<Option<User>> execute(Query query) {
        return Try.of(() -> loadUser.loadUser(query.id()));
    }
}
