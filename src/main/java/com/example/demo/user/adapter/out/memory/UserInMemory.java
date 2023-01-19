package com.example.demo.user.adapter.out.memory;

import com.example.demo.user.domain.User;
import com.example.demo.user.ports.out.LoadUser;
import com.example.demo.user.ports.out.SaveUser;
import com.example.demo.user.ports.out.UserNameExists;
import io.vavr.control.Option;

import java.util.HashMap;


public class UserInMemory implements LoadUser, SaveUser, UserNameExists {

    private final HashMap<String, User> entries;

    public UserInMemory() {
        entries = new HashMap<>();
    }

    @Override
    public Option<User> loadUser(String id) {
        return Option.of(entries.get(id));
    }

    @Override
    public void saveUser(User user) {
        entries.put(user.id(), user);
    }

    @Override
    public boolean userNameExists(String username) {
        return entries.values().stream().anyMatch(user -> user.name().equals(username));
    }
}
