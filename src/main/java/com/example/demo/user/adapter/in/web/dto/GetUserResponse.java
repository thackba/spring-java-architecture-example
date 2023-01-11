package com.example.demo.user.adapter.in.web.dto;

import com.example.demo.user.domain.User;

public record GetUserResponse(String id, String name) {

    public static GetUserResponse fromDomain(User user) {
        return new GetUserResponse(user.id(), user.name());
    }
}
