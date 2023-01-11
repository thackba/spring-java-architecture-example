package com.example.demo.register.ports.in;

import com.example.demo.register.domain.Login;

public interface RegisterLogin {

    boolean execute(RegisterLogin.Command command);

    record Command(String name) {

        public Login toDomain() {
            return new Login(this.name());
        }
    }
}
