package com.example.demo.user.adapter.in.web.dto;

import com.example.demo.user.ports.in.AddUser;

public record AddUserResponse(String id, String message) {

    public static AddUserResponse fromDomain(AddUser.Result result) {
        return new AddUserResponse(result.id(), null);
    }
}
