package com.example.demo.user.adapter.in.web.dto;

import com.example.demo.user.ports.in.AddUser;

public record AddUserResponse(String id) {

    public static AddUserResponse fromDomain(AddUser.Result result) {
        return new AddUserResponse(result.id());
    }
}
