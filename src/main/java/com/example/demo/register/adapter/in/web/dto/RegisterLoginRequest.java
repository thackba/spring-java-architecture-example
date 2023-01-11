package com.example.demo.register.adapter.in.web.dto;

import com.example.demo.register.ports.in.RegisterLogin;

public record RegisterLoginRequest(String name) {

    public RegisterLogin.Command toCommand() {
        return new RegisterLogin.Command(this.name());
    }
}
