package com.example.demo.user.adapter.in.web.dto;

import com.example.demo.user.ports.in.AddUser;

public record AddUserRequest(String name) {
    public AddUser.Command toCommand() {
        return new AddUser.Command(this.name);
    }
}
