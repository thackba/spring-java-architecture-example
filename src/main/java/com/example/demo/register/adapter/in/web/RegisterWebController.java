package com.example.demo.register.adapter.in.web;

import com.example.demo.register.adapter.in.web.dto.RegisterLoginRequest;
import com.example.demo.register.ports.in.RegisterLogin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterWebController {

    private final RegisterLogin registerLogin;

    public RegisterWebController(RegisterLogin registerLogin) {
        this.registerLogin = registerLogin;
    }

    @PostMapping("/")
    public ResponseEntity<Void> addUserRoute(@RequestBody RegisterLoginRequest request) {
        if (registerLogin.execute(request.toCommand())) {
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}
