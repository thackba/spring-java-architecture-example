package com.example.demo.user.app;

import com.example.demo.user.domain.User;
import com.example.demo.user.ports.in.GetUser;
import com.example.demo.user.ports.out.LoadUser;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetUserUseCase implements GetUser {

    final private LoadUser loadUser;

    public GetUserUseCase(LoadUser loadUser) {
        this.loadUser = loadUser;
    }

    @Override
    public Optional<User> execute(String id) {
        return loadUser.loadUser(id);
    }
}
